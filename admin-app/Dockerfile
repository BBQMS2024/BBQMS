FROM node:21-alpine AS build
WORKDIR /admin-app
COPY . ./
RUN npm install
CMD ["npm", "run", "dev"]
