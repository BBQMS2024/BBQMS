FROM node:21-alpine AS build
WORKDIR /admin-app
COPY package*.json .
RUN npm install

COPY . .
RUN npm run build

EXPOSE 5001
CMD ["npm", "run", "preview"]
