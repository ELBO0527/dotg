import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'Dotg',
  description: '우',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
       <head>
        <link rel="icon" href="/favicon.ico" sizes="any" />
        
        <script
          dangerouslySetInnerHTML={{
            __html: `
        (function() {
          const theme = localStorage.getItem('theme');
          if (theme === 'dark' || (!theme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
            document.documentElement.classList.add('dark');
          } else {
            document.documentElement.classList.remove('dark');
          }
        })();
        `}}
        />

      </head>
      <body className={inter.className}>{children}</body>
    </html>
  )
}