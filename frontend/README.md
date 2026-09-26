# TaskFlow Frontend

Frontend for the Spring Boot Task Management System.

## Stack
HTML5, CSS3, JavaScript, Bootstrap 5.3.3, Bootstrap Icons.

## Backend
The frontend expects the backend at `http://localhost:8080` and API base `http://localhost:8080/api`.

## Pages
- `index.html` — Login
- `register.html` — Registration
- `dashboard.html` — Dashboard, task list and CRUD actions
- `add-task.html` — Create task
- `edit-task.html?id=ID` — Edit task

## Run
Start the Spring Boot backend first. Then serve this folder using IntelliJ Live Server or another local static server. Do not open the HTML through an arbitrary production deployment yet.

The backend CORS configuration already allows localhost ports 5500 and 3000.

JWT is stored in browser localStorage for this demo/resume project and sent as `Authorization: Bearer <token>`. Production deployments should use HTTPS and review client-side token storage/session protections.
