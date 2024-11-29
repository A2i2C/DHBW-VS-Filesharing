FROM node:18-alpine

WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install -g @angular/cli
RUN npm set strict-ssl false
RUN npm install
RUN npm set strict-ssl true
COPY . .
EXPOSE 4200

# Default command to serve the Angular app
CMD ["ng", "serve", "--host", "0.0.0.0"]
