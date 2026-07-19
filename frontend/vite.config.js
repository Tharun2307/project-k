import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  build: {
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        login: resolve(__dirname, 'login.html'),
        admin: resolve(__dirname, 'admin.html'),
        scoring: resolve(__dirname, 'scoring.html'),
      },
    },
  },
  server: {
    port: 5173,
    host: true
  }
});