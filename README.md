# 🔥 Lit Envs — Environment Variable Sync CLI

**Lit Envs** is a developer tool that allows teams to **securely push and pull `.env` files** across projects. It's designed to keep environment variables synchronized among team members, with simple commands powered by a Go CLI and a Java (Spring Boot) backend.

---

## ✨ Features

- 🔐 Login via email/password (JWT-based)
- 📁 Select and switch between projects
- 📥 Pull environment files from the server
- 📤 Push updated `.env` files to the server
- 📝 Config stored locally using TOML format
- 🧩 Built using:
  - Go (CLI)
  - Java Spring Boot (Backend API)
  - Optional legacy CLI in Python

---

## ⚙️ How It Works

1. **Login** to store your access token locally.
2. **Select a project** to work with.
3. Use `pull` to download the project’s `.env` file.
4. Use `push` to upload your local `.env` file to the backend.
5. The config is stored locally in `.lit_env_data.toml`.

---

## 🛠️ Setup Instructions

### 🔧 Requirements

- Go 1.21+
- Java 17+ (for backend API)
- Python 3.8+ (if using the old CLI)
- Linux, macOS, or Windows (WSL recommended)

---

### 🐹 Install Go CLI

```bash
git clone https://github.com/your-username/lit-envs.git
cd lit-envs/cli-go
go build -o lit
The lit binary will be created in the current directory.
```
🚀 Usage
bash
Copy
Edit

```bash
./lit login             # Login and save JWT token
./lit select-project    # Choose active project from server
./lit pull              # Download .env file for selected project
./lit push -f .env      # Upload .env file for selected project
```

🗂️ Local Configuration (.lit_env_data.toml)
The CLI stores your session and project info in:

