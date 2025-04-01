import json
import requests
import random
import time

SERVERS_SERVER_IP = "172.16.198.33"
USER_ID = 1

metoca = False
activo = True

def get_servers():
    try:
        api_url = f'http://{SERVERS_SERVER_IP}:5000/servers'
        response = requests.get(api_url)
        response.raise_for_status()
        servers = response.json()
        return servers
    except Exception as e:
        print(f"Error retrieving server list from API: {e}")
        return None


while activo:
    servidores = get_servers()

    if metoca:
        num = random.randint(1, len(servidores))
        URL = servidores[num - 1] + '/tetoca'
        respuesta = requests.get(URL)
        print(respuesta.content)
        metoca = False
    else:
        for s in servidores:
            print(f'Llamando al servidor {s}')
            time.sleep(1)
            URL = s + f'/metoca?id={USER_ID}'
            respuesta = requests.get(URL)
            dic = json.loads(respuesta.content)
            print(dic)
            if 'SI' in dic.get('res', ''):
                metoca = True
                break

    time.sleep(2)
