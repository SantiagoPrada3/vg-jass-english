**Jass Project - PRS1
Overview**

**🔧 Project Stack**

**Backend**: Java 17 (IntelliJ IDEA, Spring Boot)
**Frontend**: Angular (latest stable version)
**Database**: MongoDB

**✅ Project Purpose**

This PRS initiative from our Valle Grande Institute offers benefits to the residents of Nuevo de Conta - Bella Vista, providing community support using real-world technology tools, enabling them to build and maintain their own JASS computer systems.


**🛠️ Setup Instructions (Imperatives)**

1. Clone the repository:
    git clone https://github.com/YourOrg/cetpro-social-project.git
2. Navigate into backend:
    cd cetpro-social-project/backend
3. Run Spring Boot app:
    ./mvnw spring-boot:run
4. Navigate into frontend:
    cd ../frontend
5. Install dependencies and serve the Angular app:
    npm install
    ng serve

🧩 How to Use the App (Advice with “should”)

- You **should** open http://localhost:4200 after both backend and frontend are running.
- You **should** create a user account or admin to access the system.

**🎯 Future Plans (Advice & Suggestions)**

- We **should** implement user roles (superadmin, admin, client) to control content access.
- We **should** schedule workshops on using the system.

**📁 Repository Structure**

/vg-ms-boxes-assignment
├── backend/        # Java 17 + Spring Boot REST API
├── frontend/       # Angular app
├── README.md       # ← You are here
├── CONTRIBUTING.md # Contribution guidelines
├── .env.example    # Environment variables template
└── docs/           # Project documentation & diagrams

**🧑‍🏫 Contributing (Imperatives & Advice)**

- Fork this repo.
- Create a feature branch:
  git checkout -b feature/boxes-assignment
- Implement, test, and lint your feature locally.
- Open a Pull Request with a clear summary and description.
  You should add “Fixes #<issue-number>” in your PRS if it's related to an open issue.

**🚀 Deployment Requirements (Must & Need To)**

- You must set the environment variables:
- MONGODB_URI=your_mongodb_uri
- JWT_SECRET=your_jwt_secret_key
- You need to enable CORS in the Spring configuration for frontend access.
- You must build the frontend before deployment:
  npm run build in /frontend/
Upload contents of dist/ to your hosting platform.

**💡 Best Practices & Tips**

- You **should** write unit tests (JUnit for backend, Jasmine/Karma for frontend).
- You **should** document any new REST endpoints in the README or API specification.
- You **should** run mvn clean and npm run lint before each commit.

**📞 Questions & Support**

If you need help:

- Open an issue in this repository.
- Tag @project-lead for urgent issues.
- Join our group chat on Discord or Telegram for real-time collaboration.

  
