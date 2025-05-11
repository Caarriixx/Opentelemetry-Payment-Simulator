# Usar una imagen base de Node.js para instalar Angular
FROM node:18.19

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos necesarios
COPY package*.json ./

# Instalar dependencias de Angular
RUN npm install -g @angular/cli@14.2.13 && npm install

# Copiar el código del proyecto al contenedor
COPY . .

# Exponer el puerto 4200
EXPOSE 4200

# Comando para iniciar la aplicación Angular
CMD ["ng", "serve", "--host", "0.0.0.0"]
