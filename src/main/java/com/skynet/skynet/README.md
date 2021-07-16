# Skynet #
Service Delivery Demo to optimally bind processes to machines designed to run them

Version 2.0 (this one as of june - july 2021) allows for the user to load in processes, machines, and links between said elements from files to create a system binding elements together as balanced as possible. 

Processes from files must be loaded in as a MAP, not as a LIST, otherwise everything breaks, at least in this version.
The map has keys as the process ids, and the values as the process themselves.

Each process follows this schema:

code()
"type": "object",
      "properties": {
        "iD": {
          "type": "integer"
        },
        "types": {
          "type": "array",
          "items": [
            {
              "type": "integer"
            }
          ]
        },
        "servReq": {
          "type": "array",
          "items": [
            {
              "type": "integer"
            }
          ]
        },
        "servGiv": {
          "type": "array",
          "items": [
            {
              "type": "integer"
            }
          ]
        },
        "binded": {
          "type": "boolean"
        },
        //map: keys is string name of rss, vals is # of rss
        "numRss": {
          "type": "object",
          "properties": {
            "generic rss 1": {
              "type": "integer"
            }
          }
        }
      },
      "required": [
        "iD",
        "types",
        "servReq",
        "servGiv",
        "binded",
        "numRss"
      ]
    }