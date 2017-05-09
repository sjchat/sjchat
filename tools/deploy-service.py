#!/usr/bin/env python3
from http.server import BaseHTTPRequestHandler, HTTPServer
import subprocess
 
password = ''

def deploy():
    subprocess.Popen(['sh', 'deploy-script.sh'])
    

class RequestHandler(BaseHTTPRequestHandler):
    def handleDeploy(self):
        # Send response status code
        self.send_response(200)

        # Send headers
        self.send_header('Content-type','text/html')
        self.end_headers()

        message = 'Deploying...'

        # Write content as utf-8 data
        self.wfile.write(bytes(message, 'utf8'))
        deploy()


    def handleInvalidCode(self):
        self.send_response(401)
        # Send headers
        self.send_header('Content-type','text/html')
        self.end_headers()

        message = 'Invalid deploy code'

        # Write content as utf-8 data
        self.wfile.write(bytes(message, 'utf8'))


    def do_GET(self):
        if self.path == '/deploy' + password:
            self.handleDeploy()
        else:
            self.handleInvalidCode()


def run():
    # Load deploy password
    global password
    try:
        f = open('password',  'r')
        password = f.read().strip()
    except:
        print("Error reading password file")
        return

    # Server settings
    print('Setting up...')    
    address = ('', 50053)
    server = HTTPServer(address, RequestHandler)
    server.password = password

    print('Starting server...')
    server.serve_forever()

 
run()
