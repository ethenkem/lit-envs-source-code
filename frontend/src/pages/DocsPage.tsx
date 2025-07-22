import React from 'react';
import { Download, Terminal, Play } from 'lucide-react';
import { Link } from 'react-router-dom';

const DocsPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-white dark:bg-gray-900 py-16 px-6">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-6">
          Getting Started with Lit CLI
        </h1>
        <p className="text-lg text-gray-700 dark:text-gray-300 mb-8">
          Follow these steps to integrate Lit CLI into your project and start managing your environment variables securely.
        </p>

        <div className="space-y-12">

          {/* Step 1: Download */}
          <div className="flex items-start gap-4">
            <div className="p-3 bg-blue-100 dark:bg-blue-900/20 rounded-lg">
              <Download className="w-6 h-6 text-blue-600 dark:text-blue-400" />
            </div>
            <div>
              <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-2">1. Download the CLI Binary</h2>
              <p className="text-gray-600 dark:text-gray-300">
                Head over to the{' '}
                <Link
                  to="https://github.com/ethenkem/lit-source-codes/releases"
                  target="_blank"
                  className="text-blue-600 hover:underline"
                >
                  download section
                </Link>{' '}
                on GitHub, grab the latest version of the <code className="font-mono">li</code> binary for your OS,
                and place it in your project root.
              </p>
            </div>
          </div>

          {/* Step 2: Make it Executable (Optional for Unix) */}
          <div className="flex items-start gap-4">
            <div className="p-3 bg-blue-100 dark:bg-blue-900/20 rounded-lg">
              <Terminal className="w-6 h-6 text-blue-600 dark:text-blue-400" />
            </div>
            <div>
              <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-2">2. Make it Executable</h2>
              <p className="text-gray-600 dark:text-gray-300">
                On Linux/macOS, run:
              </p>
              <pre className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg text-sm font-mono text-gray-800 dark:text-gray-100 mt-2">
                chmod +x ./li
              </pre>
            </div>
          </div>

          {/* Step 3: Initialize */}
          <div className="flex items-start gap-4">
            <div className="p-3 bg-blue-100 dark:bg-blue-900/20 rounded-lg">
              <Play className="w-6 h-6 text-blue-600 dark:text-blue-400" />
            </div>
            <div>
              <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-2">3. Initialize Lit CLI</h2>
              <p className="text-gray-600 dark:text-gray-300 mb-2">
                Run the following command to start configuring your environment:
              </p>
              <pre className="bg-gray-100 dark:bg-gray-800 p-4 rounded-lg text-sm font-mono text-gray-800 dark:text-gray-100">
                ./li init
              </pre>
              <p className="text-gray-600 dark:text-gray-300 mt-2">
                This will walk you through setting up your project and securely syncing your variables.
              </p>
            </div>
          </div>
        </div>

        <div className="mt-16 text-center">
          <Link
            to="/dashboard"
            className="inline-block bg-blue-600 text-white px-6 py-3 rounded-lg text-lg font-semibold hover:bg-blue-700 transition-colors"
          >
            Go to Dashboard
          </Link>
        </div>
      </div>
    </div>
  );
};

export default DocsPage;
