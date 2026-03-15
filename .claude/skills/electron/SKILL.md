---
name: electron
description: Electron desktop app reference — security model, IPC patterns, context isolation, packaging, and auto-update. Use when working on desktop app features.
---

# Electron Reference

## Security Model (Critical)

### Context Isolation (always enabled)
```javascript
new BrowserWindow({
  webPreferences: {
    contextIsolation: true,     // mandatory
    nodeIntegration: false,     // mandatory
    sandbox: true,              // default in Electron 20+
    preload: path.join(__dirname, 'preload.js')
  }
});
```

### Preload Script (only bridge)
```javascript
// preload.js
const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  readFile: (path) => ipcRenderer.invoke('fs:read', path),
  onUpdate: (callback) => ipcRenderer.on('update-available', callback)
});
```

Never expose `ipcRenderer` directly — always wrap in `contextBridge.exposeInMainWorld()`.

### Content Security Policy
```html
<meta http-equiv="Content-Security-Policy" content="default-src 'self'; script-src 'self'">
```
No `unsafe-eval` in production. No `unsafe-inline` for scripts.

## IPC Communication

### Renderer → Main (preferred: invoke/handle)
```javascript
// Main
ipcMain.handle('fs:read', async (event, filePath) => {
  return await fs.readFile(filePath, 'utf-8');
});

// Renderer (via preload bridge)
const content = await window.electronAPI.readFile('/path/to/file');
```

### Main → Renderer
```javascript
// Main
mainWindow.webContents.send('update-available', version);

// Preload
ipcRenderer.on('update-available', (event, version) => { callback(version); });
```

### Rules
- Always use `invoke`/`handle` (async) over `send`/`on` when expecting a response
- Never use `ipcRenderer.sendSync` — blocks the renderer process
- Define channel names as constants to prevent typos
- Validate all IPC inputs in the main process

## Main Process Responsibilities

- Window management (BrowserWindow creation, lifecycle, state persistence)
- File system access (dialog.showOpenDialog, fs operations)
- Native menus (Menu, MenuItem, context menus)
- System tray integration
- Auto-updater (electron-updater)
- Notifications (Notification API)
- Protocol handlers (deep linking)

## Renderer Process

- Web content only (HTML/CSS/JS or ClojureScript)
- No direct Node.js API access
- All system interaction via preload bridge
- Same-origin policy applies

## ClojureScript / Shadow-cljs Specifics

- Shadow-cljs targets: `:electron-main` and `:electron-renderer`
- Hot reload: `shadow-cljs watch app` for renderer, restart for main
- CLJS interop with Electron APIs happens exclusively through preload bridge
- State management: re-frame or similar in renderer, atoms in main

## Packaging and Distribution

- **electron-builder** or **electron-forge** for packaging
- Platform outputs: `.dmg` (macOS), `.AppImage`/`.deb` (Linux), `.exe`/NSIS (Windows)
- Code signing: macOS notarization required for distribution, Windows Authenticode recommended
- Auto-update: `electron-updater` with GitHub Releases as update server
- Always test packaged build before release (dev mode hides packaging issues)

## Performance

- Lazy window creation (don't create all windows on startup)
- Background throttling: Electron throttles hidden windows — account for this in background tasks
- Memory: monitor renderer process memory, avoid leaks from event listeners and closures
- Startup: defer non-essential initialization, show window early with loading state

## Common Pitfalls

- **Remote module:** removed in recent Electron — do not use
- **shell.openExternal:** use for external links — never navigate the main window to external URLs
- **Multiple windows:** each window has its own renderer process and preload
- **File paths:** use `app.getPath('userData')` for persistent storage, not hardcoded paths
- **Native modules:** must be rebuilt for Electron's Node.js version (`electron-rebuild`)
