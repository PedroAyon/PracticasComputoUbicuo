#Importar librerias
import json
import requests
import random
import time

metoca = False

activo = True

while activo == True:
    servidor = [f"http://172.16.198.33:{port}" for port in range(3000, 3022)]
    if metoca:
        num = random.randint(1, len(servidor))

        URL = servidor[num-1] + '/tetoca'
 
        respuesta = requests.get(URL)
    
        print(respuesta.content)
        metoca = False
    else:

        metoca = False
        for s in servidor:
            time.sleep(1)

            URL = s + '/metoca?id=5'

            respuesta = requests.get(URL)

            dic = json.loads(respuesta.content)

            print( dic )
            
            if 'SI' in dic['res']:
                metoca = True
                break

    time.sleep(2)
