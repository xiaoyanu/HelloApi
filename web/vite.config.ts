import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'
import tailwindcss from '@tailwindcss/vite'
// https://vite.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        vueDevTools(),
        tailwindcss(),
        AutoImport({
            resolvers: [
                ElementPlusResolver()
            ],
        }),
        Components({
            resolvers: [
                ElementPlusResolver()
            ],
        }),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        },
    },
    build: {
        rollupOptions: {
            output: {
                manualChunks(id) {
                    // 将 echarts 相关的包单独打包成一个 chunk
                    if (id.includes('node_modules/echarts')) {
                        return 'echarts';
                    }
                    // 将 vue 相关的包单独打包成一个 chunk
                    if (id.includes('node_modules/vue')) {
                        return 'vue-vendor';
                    }
                }
            },
        }
    }
})
