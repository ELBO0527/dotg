/** @type {import('next').NextConfig} */

module.exports = {
    output: 'standalone',
    async rewrites() {
        return [
            {
                source: "/:path*",
                destination: "http://localhost:29929/:path*",
            },
        ];
    }
}

//module.exports = nextConfig
