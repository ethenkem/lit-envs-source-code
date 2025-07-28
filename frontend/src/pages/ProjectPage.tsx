import React, { useCallback, useState } from 'react';
import { useParams, Link, useNavigate, useLocation } from 'react-router-dom';
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
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>(mockTeamMembers);
  const [showEncrypted, setShowEncrypted] = useState(false);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [copiedId, setCopiedId] = useState<string | null>(null);
  const location = useLocation()
  const project = location.state.project

  const handleAddVariable = (key: string, value: string) => {
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
                {project.projectName}
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
          <div className="bg-white items-center justify-center flex dark:bg-gray-800 overflow-hidden shadow rounded-lg">
            <Shield size={40} className=" text-green-500" />
            <p className="text-lg ml-2 font-medium text-gray-500 dark:text-gray-400 truncate">
              Variables Highly Encrypted
            </p>
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
                      {project.collaborators.length}
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
                  className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${showEncrypted
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
          </div>

          <div className="divide-y divide-gray-200 dark:divide-gray-700">
            <div className='px-7 py-8 overflow-hidden'>
              <textarea
                readOnly
                wrap="soft"
                className="w-full h-64 p-4 border text-gray-200 outline-none bg-gray-900 border-gray-700 rounded resize-none overflow-auto font-mono"
              >
                {project.dotEnvData}
              </textarea>
            </div>
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
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${member.role === 'owner'
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
        projectId={project.id}
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onSubmit={handleAddVariable}
      />

      <InviteUserModal
        isOpen={isInviteModalOpen}
        onClose={() => setIsInviteModalOpen(false)}
        onSubmit={handleInviteUser}
        projectId={project.id}
      />
    </DashboardLayout>
  );
};

export default ProjectPage;
