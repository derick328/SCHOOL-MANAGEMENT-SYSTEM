# 🚀 Deployment Checklist

## Pre-Deployment

### ✅ Code Review
- [ ] All code is committed to version control
- [ ] No hardcoded credentials in code
- [ ] No TODO or FIXME comments in critical paths
- [ ] All tests pass
- [ ] Code follows consistent style guidelines

### ✅ Security Configuration

#### JWT Settings
- [ ] Change JWT secret from default value
  - Generate strong secret: `openssl rand -base64 64`
  - Update in `application.yml` or environment variables
  - **DO NOT** commit secrets to git

#### Default Credentials
- [ ] Change default admin password (admin/Admin@123)
- [ ] Remove or disable demo accounts (teacher_demo, student_demo)
- [ ] Ensure all users have strong passwords

#### Database Security
- [ ] Use dedicated database user (not postgres superuser)
- [ ] Use strong database password
- [ ] Configure PostgreSQL for network access control
- [ ] Enable SSL for database connections

#### Application Security
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not update/create)
- [ ] Disable detailed error messages in production
- [ ] Configure CORS for specific domains only
- [ ] Set secure session configuration
- [ ] Enable HTTPS/TLS
- [ ] Configure security headers

### ✅ Configuration

#### Application Properties
- [ ] Update `application.yml` for production:
  ```yaml
  spring:
    jpa:
      hibernate:
        ddl-auto: validate  # Change from 'update'
      show-sql: false       # Disable SQL logging
  
  logging:
    level:
      root: WARN
      com.school.sms: INFO
  ```

#### Environment Variables
- [ ] Set up environment variables:
  - `JWT_SECRET`
  - `DATABASE_URL`
  - `DATABASE_USERNAME`
  - `DATABASE_PASSWORD`
  - `SMTP_HOST`, `SMTP_USERNAME`, `SMTP_PASSWORD`

#### Email Configuration
- [ ] Configure SMTP settings
- [ ] Test email sending
- [ ] Set up email templates

### ✅ Database

#### Database Setup
- [ ] Create production database
- [ ] Run database migrations
- [ ] Set up database backups
- [ ] Configure connection pooling
- [ ] Create database indexes
- [ ] Test database connectivity

#### Data Migration
- [ ] Backup existing data (if any)
- [ ] Test data migration scripts
- [ ] Verify data integrity

### ✅ Testing

#### Functional Testing
- [ ] Test all authentication flows
- [ ] Test role-based access control
- [ ] Test password reset flow
- [ ] Test API endpoints
- [ ] Test error handling

#### Security Testing
- [ ] Test SQL injection prevention
- [ ] Test XSS prevention
- [ ] Test CSRF protection
- [ ] Test authentication bypass attempts
- [ ] Test authorization bypass attempts
- [ ] Verify secure headers

#### Performance Testing
- [ ] Load testing with expected user count
- [ ] Database query optimization
- [ ] Check for N+1 query problems
- [ ] Test under high concurrent requests

## Deployment

### ✅ Build

```bash
# Clean build
mvn clean package -DskipTests

# Build with tests
mvn clean package

# Verify JAR is created
ls -lh target/*.jar
```

### ✅ Server Setup

#### System Requirements
- [ ] Java 17+ installed
- [ ] PostgreSQL 12+ installed
- [ ] Sufficient disk space (min 10GB)
- [ ] Sufficient RAM (min 2GB)

#### Application Server
- [ ] Create application user (non-root)
- [ ] Set up application directory
- [ ] Configure file permissions
- [ ] Set up log rotation
- [ ] Configure firewall rules

#### Reverse Proxy (Nginx/Apache)
- [ ] Install and configure reverse proxy
- [ ] Set up SSL certificate (Let's Encrypt recommended)
- [ ] Configure HTTPS redirect
- [ ] Set up rate limiting
- [ ] Configure static file serving

### ✅ Deployment Steps

1. **Upload Application**
   ```bash
   scp target/school-management-system-1.0.0.jar user@server:/opt/sms/
   ```

2. **Create Systemd Service**
   ```bash
   sudo nano /etc/systemd/system/sms.service
   ```

   ```ini
   [Unit]
   Description=School Management System
   After=syslog.target network.target

   [Service]
   User=sms
   ExecStart=/usr/bin/java -jar /opt/sms/school-management-system-1.0.0.jar
   SuccessExitStatus=143
   Restart=on-failure
   RestartSec=10

   [Install]
   WantedBy=multi-user.target
   ```

3. **Start Service**
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl start sms
   sudo systemctl enable sms
   sudo systemctl status sms
   ```

### ✅ Monitoring

#### Application Monitoring
- [ ] Set up application health checks
- [ ] Configure Spring Boot Actuator endpoints
- [ ] Set up logging aggregation
- [ ] Configure error alerting

#### Server Monitoring
- [ ] Monitor CPU usage
- [ ] Monitor memory usage
- [ ] Monitor disk space
- [ ] Monitor network traffic

#### Database Monitoring
- [ ] Monitor database connections
- [ ] Monitor slow queries
- [ ] Monitor database size
- [ ] Set up automated backups

## Post-Deployment

### ✅ Verification

- [ ] Access application via browser
- [ ] Test login with admin credentials
- [ ] Verify all API endpoints work
- [ ] Check logs for errors
- [ ] Test SSL certificate
- [ ] Verify database connectivity

### ✅ Documentation

- [ ] Update deployment documentation
- [ ] Document server credentials (securely)
- [ ] Create runbook for common issues
- [ ] Document backup/restore procedures
- [ ] Create disaster recovery plan

### ✅ Backup Strategy

- [ ] Set up automated database backups
- [ ] Test backup restoration
- [ ] Configure off-site backup storage
- [ ] Document backup schedule
- [ ] Test disaster recovery procedure

### ✅ Maintenance

- [ ] Schedule regular security updates
- [ ] Plan for database maintenance windows
- [ ] Set up automated log rotation
- [ ] Configure monitoring alerts
- [ ] Plan for scaling strategy

## Production Environment Variables Example

```bash
# Create .env file (never commit this!)
export JWT_SECRET="your-super-secure-secret-key-here-min-256-bits"
export DATABASE_URL="jdbc:postgresql://localhost:5432/school_management_db"
export DATABASE_USERNAME="sms_user"
export DATABASE_PASSWORD="your-secure-database-password"
export SMTP_HOST="smtp.gmail.com"
export SMTP_PORT="587"
export SMTP_USERNAME="your-email@gmail.com"
export SMTP_PASSWORD="your-app-specific-password"
export SERVER_PORT="8080"
export SPRING_PROFILES_ACTIVE="prod"
```

## Common Issues & Solutions

### Issue: Application won't start
- Check logs: `journalctl -u sms -n 50`
- Verify Java version: `java -version`
- Check database connectivity
- Verify environment variables

### Issue: Database connection errors
- Check PostgreSQL is running
- Verify connection string
- Check firewall rules
- Verify credentials

### Issue: High memory usage
- Adjust JVM heap size: `-Xms512m -Xmx2g`
- Check for memory leaks
- Review query performance
- Enable garbage collection logging

## Security Reminders

🔒 **CRITICAL**:
- Change ALL default passwords
- Use strong JWT secret (min 256 bits)
- Enable HTTPS only
- Keep dependencies updated
- Monitor security advisories
- Regular security audits
- Keep backups secure
- Use environment variables for secrets
- Enable audit logging
- Review access logs regularly

## Support & Maintenance

- [ ] Set up support email/system
- [ ] Create escalation procedures
- [ ] Schedule regular maintenance windows
- [ ] Plan for security patches
- [ ] Set up on-call rotation (if applicable)

---

**Remember**: Security is not a one-time task. Regular reviews and updates are essential!

✅ All checked? You're ready to deploy!

---

**For questions or issues, refer to**:
- README.md
- PROJECT_STRUCTURE.md
- Application logs
