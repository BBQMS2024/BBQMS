FROM node:21-alpine AS build
WORKDIR /teller-app
COPY . ./
RUN npm install
CMD ["npm", "run", "dev"]
