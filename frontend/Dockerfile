# Stage 1 - Build the React application
FROM node:18-alpine as react-build
WORKDIR /frontend/

COPY public /frontend/public/
COPY src/ /frontend/src
COPY package.json /frontend/
COPY package.json .
COPY package-lock.json .
COPY tsconfig.json .
COPY tailwind.config.js .
RUN npm install
RUN npm run build


# Stage 2 - Serve the React application using NGINX
FROM nginx:1.19.0
WORKDIR /usr/share/nginx/html

# Copy the built React application from the first stage
COPY --from=react-build /frontend/build/ /usr/share/nginx/html


ENTRYPOINT ["nginx", "-g", "daemon off;"]
