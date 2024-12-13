FROM node:alpine AS build

WORKDIR /app

COPY package*.json /app/

RUN npm install -g @angular/cli

RUN npm install
COPY . .
EXPOSE 4200

# Default command to serve the Angular app
CMD ["ng", "serve", "--host", "0.0.0.0"]
