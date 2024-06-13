FROM node:21-alpine AS build
WORKDIR /teller-app
COPY package*.json .
RUN npm install

COPY . .
RUN npm run build

EXPOSE 3000
CMD ["npm", "run", "preview"]
