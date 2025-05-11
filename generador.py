import requests
import random
import string

# Genera un id aleatorio (una cadena de 10 caracteres)
def generate_id():
    return ''.join(random.choices(string.ascii_letters + string.digits, k=10))

# Genera un origin aleatorio (una cadena de 3 letras)
def generate_origin():
    return ''.join(random.choices(string.ascii_uppercase, k=3))

# Genera un destination aleatorio (una cadena de 3 letras)
def generate_destination():
    return ''.join(random.choices(string.ascii_uppercase, k=3))

# Genera un amount aleatorio (una cadena que simula una cantidad monetaria)
def generate_amount():
    return round(random.uniform(10, 1000), 2)

# Genera un estado aleatorio (un número entero entre 0 y 5)
def generate_state():
    return random.randint(0, 5)

# Función principal para hacer la petición
def send_post_request(url):
    data = {
        "id": generate_id(),
        "origin": generate_origin(),
        "destination": generate_destination(),
        "amount": generate_amount(),
        "state": generate_state()
    }
    
    # Realiza la petición POST
    response = requests.post(url, json=data)
    
    # Imprime el resultado de la petición
    if response.status_code == 200:
        print(f"Petición enviada con éxito: {data}")
    else:
        print(f"Error {response.status_code}: {response.text}")

# URL a la que se enviarán las peticiones
url = "http://localhost:8081/api/v1/log/insert"

# Ejemplo de envío de peticiones en bucle (puedes controlar cuántas veces se ejecuta)
for _ in range(1000):  # Cambia el 5 por el número de peticiones que quieras hacer
    send_post_request(url)
