import React, { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { 
  ArrowLeft, 
  Plus, 
  Shield, 
  Eye, 
  EyeOff, 
  Edit3, 
  Trash2, 
  Users, 
  Terminal, 
  Copy,
  Check,
  Search,
  Filter
} from 'lucide-react';
import DashboardLayout from '../components/Layout/DashboardLayout';
import AddVariableModal from '../components/AddVariableModal';
import InviteUserModal from '../components/InviteUserModal';

interface EnvVariable {
  id: string;
  key: string;
  value: string;
  encryptedValue: string;
  lastUpdated: string;
  updatedBy: string;
}

interface TeamMember {
  id: string;
  name: string;
  email: string;
  avatar: string;
  role: 'owner' | 'editor' | 'viewer';
}

const mockVariables: EnvVariable[] = [
  {
    id: '1',
    key: 'DATABASE_URL',
    value: 'postgresql://user:pass@localhost:5432/mydb',
    encryptedValue: 'a94f8fe3e8422ef2c7b6d5a9f1b2c3d4e5f6a7b8c9d0e1f2',
    lastUpdated: '2 hours ago',
    updatedBy: 'John Doe'
  },
  {
    id: '2',
    key: 'API_SECRET_KEY',
    value: 'sk_live_abcd1234efgh5678ijkl9012mnop3456',
    encryptedValue: 'b5c8f2e9a1d4e7b0c3f6a9d2e5f8b1c4e7a0d3f6b9c2e5f8',
    lastUpdated: '1 day ago',
    updatedBy: 'Jane Smith'
  },
  {
    id: '3',
    key: 'JWT_SECRET',
    value: 'super-secret-jwt-key-for-token-signing',
    encryptedValue: 'c6d9f3e0b2e5f8c1d4f7b0e3f6c9d2f5f8c1e4f7b0d3f6c9',
    lastUpdated: '3 days ago',
    updatedBy: 'Bob Wilson'
  }
];

const mockTeamMembers: TeamMember[] = [
  {
    id: '1',
    name: 'John Doe',
    email: 'john@example.com',
    avatar: 'https://ui-avatars.com/api/?name=John+Doe&background=3B82F6&color=fff',
    role: 'owner'
  },
  {
    id: '2',
    name: 'Jane Smith',
    email: 'jane@example.com',
    avatar: 'https://ui-avatars.com/api/?name=Jane+Smith&background=10B981&color=fff',
    role: 'editor'
  },
  {
    id: '3',
    name: 'Bob Wilson',
    email: 'bob@example.com',
    avatar: 'https://ui-avatars.com/api/?name=Bob+Wilson&background=F59E0B&color=fff',
    role: 'viewer'
  }
];

const ProjectPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [variables, setVariables] = useState<EnvVariable[]>(mockVariables);
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>(mockTeamMembers);
  const [showEncrypted, setShowEncrypted] = useState(false);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [copiedId, setCopiedId] = useState<string | null>(null);

  const project = {
    name: 'E-commerce API',
    description: 'Environment variables for the main e-commerce platform'
  };

  const filteredVariables = variables.filter(variable =>
    variable.key.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleAddVariable = (key: string, value: string) => {
    const newVariable: EnvVariable = {
      id: Date.now().toString(),
      key,
      value,
      encryptedValue: 'encrypted_' + Math.random().toString(36).substring(2, 15),
      lastUpdated: 'Just now',
      updatedBy: 'You'
    };
    setVariables([newVariable, ...variables]);
  };

  const handleInviteUser = (email: string, role: 'editor' | 'viewer') => {
    const newMember: TeamMember = {
      id: Date.now().toString(),
      name: email.split('@')[0],
      email,
      avatar: `https://ui-avatars.com/api/?name=${email.split('@')[0]}&background=6366F1&color=fff`,
      role
    };
    setTeamMembers([...teamMembers, newMember]);
  };

  const copyToClipboard = async (text: string, id: string) => {
    try {
      await navigator.clipboard.writeText(text);
      setCopiedId(id);
      setTimeout(() => setCopiedId(null), 2000);
    } catch (err) {
      console.error('Failed to copy:', err);
    }
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <Link
              to="/dashboard"
              className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300"
            >
              <ArrowLeft className="h-5 w-5" />
            </Link>
            <div>
              <h1 className="text-2xl font-bold text-gray-900 dark:text-white flex items-center">
                {project.name}
                <Shield className="h-6 w-6 text-green-500 ml-2" />
              </h1>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                {project.description}
              </p>
            </div>
          </div>
          <div className="flex items-center space-x-3">
            <button
              onClick={() => setIsInviteModalOpen(true)}
              className="inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700"
            >
              <Users className="h-4 w-4 mr-2" />
              Invite
            </button>
            <button
              onClick={() => setIsAddModalOpen(true)}
              className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Variable
            </button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
          <div className="bg-white dark:bg-gray-800 overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <Shield className="h-6 w-6 text-green-500" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 dark:text-gray-400 truncate">
                      Variables
                    </dt>
                    <dd className="text-lg font-medium text-gray-900 dark:text-white">
                      {variables.length}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-gray-800 overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <Users className="h-6 w-6 text-blue-500" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 dark:text-gray-400 truncate">
                      Team Members
                    </dt>
                    <dd className="text-lg font-medium text-gray-900 dark:text-white">
                      {teamMembers.length}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-gray-800 overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <Terminal className="h-6 w-6 text-purple-500" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 dark:text-gray-400 truncate">
                      Last CLI Sync
                    </dt>
                    <dd className="text-lg font-medium text-gray-900 dark:text-white">
                      1h ago
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Environment Variables Section */}
        <div className="bg-white dark:bg-gray-800 shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200 dark:border-gray-700">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
              <h2 className="text-lg font-medium text-gray-900 dark:text-white">
                Environment Variables
              </h2>
              <div className="mt-4 sm:mt-0 flex items-center space-x-3">
                <button
                  onClick={() => setShowEncrypted(!showEncrypted)}
                  className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${
                    showEncrypted
                      ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-400'
                      : 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400'
                  }`}
                >
                  {showEncrypted ? (
                    <>
                      <EyeOff className="h-3 w-3 mr-1" />
                      Encrypted View
                    </>
                  ) : (
                    <>
                      <Eye className="h-3 w-3 mr-1" />
                      Plain View
                    </>
                  )}
                </button>
              </div>
            </div>

            <div className="mt-4 flex flex-col sm:flex-row gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search variables..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-700 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              <button className="inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                <Filter className="h-4 w-4 mr-2" />
                Filter
              </button>
            </div>
          </div>

          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {filteredVariables.map((variable) => (
              <div key={variable.id} className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center space-x-2 mb-2">
                      <h3 className="text-sm font-medium text-gray-900 dark:text-white font-mono">
                        {variable.key}
                      </h3>
                      <Shield className="h-4 w-4 text-green-500" />
                    </div>
                    
                    <div className="flex items-center space-x-2 mb-3">
                      <code className="flex-1 text-sm bg-gray-100 dark:bg-gray-700 px-3 py-2 rounded border">
                        {showEncrypted ? variable.encryptedValue : variable.value}
                      </code>
                      <button
                        onClick={() => copyToClipboard(showEncrypted ? variable.encryptedValue : variable.value, variable.id)}
                        className="p-2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
                      >
                        {copiedId === variable.id ? (
                          <Check className="h-4 w-4 text-green-500" />
                        ) : (
                          <Copy className="h-4 w-4" />
                        )}
                      </button>
                    </div>

                    <div className="flex items-center text-xs text-gray-500 dark:text-gray-400">
                      <span>Updated {variable.lastUpdated} by {variable.updatedBy}</span>
                    </div>
                  </div>

                  <div className="flex items-center space-x-2 ml-4">
                    <button className="p-2 text-gray-400 hover:text-blue-600 dark:hover:text-blue-400">
                      <Edit3 className="h-4 w-4" />
                    </button>
                    <button className="p-2 text-gray-400 hover:text-red-600 dark:hover:text-red-400">
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Team Members Section */}
        <div className="bg-white dark:bg-gray-800 shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200 dark:border-gray-700">
            <h2 className="text-lg font-medium text-gray-900 dark:text-white">
              Team Members
            </h2>
          </div>
          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            {teamMembers.map((member) => (
              <div key={member.id} className="p-6 flex items-center justify-between">
                <div className="flex items-center">
                  <img
                    className="h-10 w-10 rounded-full"
                    src={member.avatar}
                    alt={member.name}
                  />
                  <div className="ml-4">
                    <div className="text-sm font-medium text-gray-900 dark:text-white">
                      {member.name}
                    </div>
                    <div className="text-sm text-gray-500 dark:text-gray-400">
                      {member.email}
                    </div>
                  </div>
                </div>
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                  member.role === 'owner'
                    ? 'bg-purple-100 text-purple-800 dark:bg-purple-900/20 dark:text-purple-400'
                    : member.role === 'editor'
                    ? 'bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-400'
                    : 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300'
                }`}>
                  {member.role}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <AddVariableModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onSubmit={handleAddVariable}
      />

      <InviteUserModal
        isOpen={isInviteModalOpen}
        onClose={() => setIsInviteModalOpen(false)}
        onSubmit={handleInviteUser}
      />
    </DashboardLayout>
  );
};

export default ProjectPage;