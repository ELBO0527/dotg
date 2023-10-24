/** @type {import('next').NextConfig} */

module.exports = {
    output: 'standalone',
    async rewrites() {
        return [
            {
                source: "/:path*",
                destination: "http://localhost:29921/:path*",
            },
        ];
    }
}

//module.exports = nextConfig
