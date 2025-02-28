#Importar librerias
import json
import requests
import random
import time

metoca = False

activo = True

while activo == True:
    servidor = ['http://192.168.188.76:3100', 'http://192.168.188.195:3100', 'http://192.168.188.147:3100', 'http://192.168.188.181:3100']
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
