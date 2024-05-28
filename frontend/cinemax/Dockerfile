FROM node:alpine

WORKDIR /app

COPY package.json /app

# Increase memory limit for Node.js
ENV NODE_OPTIONS="--max-old-space-size=4096"

RUN npm install -g @angular/cli

RUN npm install

COPY . /app

# Expose the default Angular development server port
EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0", "--poll", "500"]