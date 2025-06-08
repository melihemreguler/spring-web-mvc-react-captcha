# BM470 Captcha Project

A Spring-based web application for handling CAPTCHA functionality, built with modern Java technologies and React frontend.

## Project Overview

This project is a full-stack web application that implements CAPTCHA functionality using Spring Framework 6.2.3 for the backend and React with Vite for the frontend. It's designed to provide secure user verification through CAPTCHA challenges.

## Technology Stack

### Backend
- **Framework**: Spring Framework 6.2.3
- **Database**: 
  - MySQL (Driver: 9.1.0)
  - PostgreSQL (Driver: 42.7.5)
- **ORM**: Hibernate 6.6.9.Final
- **Connection Pool**: C3P0 0.10.2
- **Logging**: 
  - SLF4J 2.0.16
  - Log4j2 2.24.3
- **Build Tool**: Maven
- **Java Version**: Compatible with Jakarta EE 6.0.0
- **Additional Libraries**:
  - Spring Security Core 6.4.4
  - Lombok 1.18.36
  - JSTL 3.0.0
  - JSON-lib 2.4

### Frontend
- **Framework**: React 18.2.0
- **Build Tool**: Vite 5.0.0
- **Styling**: Tailwind CSS 3.4.1
- **Routing**: React Router DOM 7.6.0
- **Icons**: Lucide React 0.372.0

## Project Structure

```
captcha-app/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/        # Java source files
│   │   │   ├── resources/   # Configuration files
│   │   │   └── webapp/      # Web application files
│   │   └── test/            # Test files
│   ├── Dockerfile           # Backend Docker configuration
│   └── pom.xml             # Maven configuration
├── frontend/
│   ├── src/                # React source files
│   ├── Dockerfile          # Frontend Docker configuration
│   ├── nginx.conf          # Nginx configuration
│   └── package.json        # NPM configuration
├── docker-compose.yml      # Docker Compose configuration
├── .github/workflows/      # GitHub Actions CI/CD
└── README.md              # Project documentation
```

## Prerequisites

- Docker and Docker Compose
- PostgreSQL RDS instance (for production)
- Domain name configured with Cloudflare
- EC2 instance with Docker installed

## Local Development

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd captcha-app
   ```

2. Create environment file:
   ```bash
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. Start the application for development:
   ```bash
   docker compose -f docker-compose.dev.yml up --build
   ```

4. Access the application:
   - Frontend: http://localhost (port 80)
   - Backend API: http://localhost:8080

## Production Deployment

The application uses a CI/CD pipeline that builds Docker images and pushes them to Docker Hub, then deploys to EC2.

### Docker Hub Images

- Backend: `melihemreguler/captcha-backend:latest`
- Frontend: `melihemreguler/captcha-frontend:latest`

## Production Deployment

### EC2 Setup

1. Install Docker and Docker Compose on EC2
2. Clone the repository to `/home/ec2-user/captcha-app`
3. Set up GitHub Secrets for CI/CD:
   - `DOCKER_HUB_USERNAME`: Your Docker Hub username
   - `DOCKER_HUB_ACCESS_TOKEN`: Your Docker Hub access token
   - `EC2_SSH_KEY`: Your EC2 private key
   - `EC2_HOST`: Your EC2 public IP
   - `EC2_USER`: ec2-user
   - `DB_HOST`: Your RDS endpoint
   - `DB_PORT`: 5432
   - `DB_NAME`: postgres
   - `DB_USER`: Your DB username
   - `DB_PASSWORD`: Your DB password
   - `LETSENCRYPT_EMAIL`: Your email for SSL certificates

### Nginx Proxy Setup

Make sure your `portfolio-nginx` repository is running with:
- nginx-proxy container
- letsencrypt companion container
- `web` network created

### DNS Configuration

Configure Cloudflare DNS:
- Type: A
- Name: captcha
- IPv4 address: Your EC2 public IP
- Proxy status: DNS only (not proxied)

## Features

- CAPTCHA generation and validation
- Database persistence with Hibernate
- RESTful API endpoints
- React-based user interface
- Automatic SSL certificate generation
- Docker containerization
- CI/CD with GitHub Actions

## Configuration

### Backend Configuration
- Database connection: `backend/src/main/resources/hibernate.properties`
- Logging: `backend/src/main/resources/log4j.properties`

### Frontend Configuration
- API endpoints: `frontend/.env`
- Build configuration: `frontend/vite.config.js`
- Styling: `frontend/tailwind.config.js`

## Testing

Run backend tests:
```bash
cd backend
mvn test
```

## Troubleshooting

### SSL Issues
If you encounter SSL handshake errors:
1. Ensure Cloudflare proxy is disabled for the subdomain
2. Check that the nginx-proxy network is running
3. Verify that LETSENCRYPT_EMAIL is correctly set

### Database Connection
If database connection fails:
1. Check RDS security group allows connection from EC2
2. Verify database credentials in environment variables
3. Ensure RDS instance is publicly accessible (if needed)

## License

This project is licensed under the MIT License.
