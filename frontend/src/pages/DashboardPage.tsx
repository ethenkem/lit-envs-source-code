import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  Plus, 
  Folder, 
  Users, 
  Shield, 
  Clock, 
  Terminal,
  Search,
  Filter
} from 'lucide-react';
import DashboardLayout from '../components/Layout/DashboardLayout';
import CreateProjectModal from '../components/CreateProjectModal';

interface Project {
  id: string;
  name: string;
  description: string;
  envCount: number;
  members: number;
  lastUpdated: string;
  isEncrypted: boolean;
  cliActivity: string;
}

const mockProjects: Project[] = [
  {
    id: '1',
    name: 'E-commerce API',
    description: 'Environment variables for the main e-commerce platform',
    envCount: 24,
    members: 5,
    lastUpdated: '2 hours ago',
    isEncrypted: true,
    cliActivity: 'Synced 1 hour ago'
  },
  {
    id: '2',
    name: 'Mobile App Backend',
    description: 'Backend services configuration for iOS and Android apps',
    envCount: 18,
    members: 3,
    lastUpdated: '1 day ago',
    isEncrypted: true,
    cliActivity: 'Synced 3 hours ago'
  },
  {
    id: '3',
    name: 'Analytics Dashboard',
    description: 'Data processing and visualization service configs',
    envCount: 12,
    members: 2,
    lastUpdated: '3 days ago',
    isEncrypted: true,
    cliActivity: 'Synced 2 days ago'
  }
];

const DashboardPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>(mockProjects);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const filteredProjects = projects.filter(project =>
    project.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    project.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreateProject = (name: string, description: string) => {
    const newProject: Project = {
      id: Date.now().toString(),
      name,
      description,
      envCount: 0,
      members: 1,
      lastUpdated: 'Just now',
      isEncrypted: true,
      cliActivity: 'Never'
    };
    setProjects([newProject, ...projects]);
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
              Your Projects
            </h1>
            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
              Manage your secure environment variables across all projects
            </p>
          </div>
          <button
            onClick={() => setIsCreateModalOpen(true)}
            className="mt-4 sm:mt-0 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            <Plus className="h-4 w-4 mr-2" />
            New Project
          </button>
        </div>

        {/* Search and Filter */}
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <input
              type="text"
              placeholder="Search projects..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="block w-full pl-10 pr-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <button className="inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700">
            <Filter className="h-4 w-4 mr-2" />
            Filter
          </button>
        </div>

        {/* Projects Grid */}
        {filteredProjects.length === 0 ? (
          <div className="text-center py-12">
            <Folder className="h-12 w-12 mx-auto text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900 dark:text-white">
              No projects found
            </h3>
            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
              {searchTerm ? 'Try adjusting your search terms.' : 'Get started by creating a new project.'}
            </p>
            {!searchTerm && (
              <div className="mt-6">
                <button
                  onClick={() => setIsCreateModalOpen(true)}
                  className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
                >
                  <Plus className="h-4 w-4 mr-2" />
                  New Project
                </button>
              </div>
            )}
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {filteredProjects.map((project) => (
              <Link
                key={project.id}
                to={`/project/${project.id}`}
                className="group block bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700 hover:border-blue-300 dark:hover:border-blue-600 hover:shadow-md transition-all duration-200"
              >
                <div className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center">
                      <Folder className="h-8 w-8 text-blue-600" />
                      {project.isEncrypted && (
                        <Shield className="h-4 w-4 text-green-500 ml-2" />
                      )}
                    </div>
                    <div className="flex items-center text-sm text-gray-500 dark:text-gray-400">
                      <Clock className="h-4 w-4 mr-1" />
                      {project.lastUpdated}
                    </div>
                  </div>

                  <h3 className="text-lg font-medium text-gray-900 dark:text-white group-hover:text-blue-600 dark:group-hover:text-blue-400">
                    {project.name}
                  </h3>
                  <p className="mt-1 text-sm text-gray-500 dark:text-gray-400 line-clamp-2">
                    {project.description}
                  </p>

                  <div className="mt-4 flex items-center justify-between text-sm">
                    <div className="flex items-center space-x-4">
                      <span className="text-gray-500 dark:text-gray-400">
                        {project.envCount} variables
                      </span>
                      <div className="flex items-center text-gray-500 dark:text-gray-400">
                        <Users className="h-4 w-4 mr-1" />
                        {project.members}
                      </div>
                    </div>
                  </div>

                  <div className="mt-3 flex items-center text-xs text-gray-500 dark:text-gray-400">
                    <Terminal className="h-3 w-3 mr-1" />
                    CLI: {project.cliActivity}
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>

      <CreateProjectModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        onSubmit={handleCreateProject}
      />
    </DashboardLayout>
  );
};

export default DashboardPage;
