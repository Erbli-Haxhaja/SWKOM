# Use the official Nginx image as the base
FROM nginx:latest

# Copy your custom nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy your frontend files to the Nginx HTML directory
COPY ./frontend /usr/share/nginx/html

# Expose port 80 for the container
EXPOSE 80

# Start Nginx when the container launches
CMD ["nginx", "-g", "daemon off;"]
