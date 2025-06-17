import React, { useState } from 'react';
import { X, Loader2, Eye, EyeOff } from 'lucide-react';

interface AddVariableModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (key: string, value: string) => void;
}

const AddVariableModal: React.FC<AddVariableModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
}) => {
  const [key, setKey] = useState('');
  const [value, setValue] = useState('');
  const [showValue, setShowValue] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));

    onSubmit(key, value);
    setKey('');
    setValue('');
    setIsLoading(false);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white dark:bg-gray-800 rounded-lg max-w-md w-full">
        <div className="flex items-center justify-between p-6 border-b border-gray-200 dark:border-gray-700">
          <h3 className="text-lg font-medium text-gray-900 dark:text-white">
            Add Environment Variable
          </h3>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          <div>
            <label htmlFor="key" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Variable Name
            </label>
            <input
              id="key"
              type="text"
              required
              value={key}
              onChange={(e) => setKey(e.target.value.toUpperCase())}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm bg-white dark:bg-gray-800 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 font-mono"
              placeholder="API_KEY"
            />
            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
              Use UPPERCASE with underscores (e.g., API_SECRET_KEY)
            </p>
          </div>

          <div>
            <label htmlFor="value" className="block text-sm font-medium text-gray-700 dark:text-gray-300">
              Value
            </label>
            <div className="mt-1 relative">
              <input
                id="value"
                type={showValue ? 'text' : 'password'}
                required
                value={value}
                onChange={(e) => setValue(e.target.value)}
                className="block w-full px-3 py-2 pr-10 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm bg-white dark:bg-gray-800 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 font-mono"
                placeholder="your-secret-value"
              />
              <button
                type="button"
                onClick={() => setShowValue(!showValue)}
                className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
              >
                {showValue ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
              </button>
            </div>
            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
              This value will be encrypted and stored securely
            </p>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-md hover:bg-gray-50 dark:hover:bg-gray-600"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading || !key.trim() || !value.trim()}
              className="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
            >
              {isLoading ? (
                <>
                  <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                  Adding...
                </>
              ) : (
                'Add Variable'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddVariableModal;