/** @type {import('next').NextConfig} */
const nextConfig = () => {
    return [
        {
            source: "/:path*",
            destination: "http://localhost:29929/:path*",
        },
    ];
}

module.exports = nextConfig
