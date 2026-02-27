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
                    // 将包单独打包

                    // echarts 相关的包
                    if (id.includes('node_modules/echarts')) {
                        return 'echarts';
                    }

                    // vue 相关的包
                    // if (id.includes('node_modules/vue')) {
                    //     return 'vue-vendor';
                    // }

                    // element-plus 相关的包
                    if (id.includes('node_modules/element-plus')) {
                        return 'element-plus';
                    }

                    // v-md-editor 相关的包
                    if (id.includes('node_modules/@kangc')) {
                        return 'v-md-editor';
                    }
                }
            },
        }
    }
})
