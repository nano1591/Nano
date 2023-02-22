const mock: Record<string, Record<string, [number, any]>> = {
  "/user": {
    "POST": [200, {
      code: 0,
      msg: "OK",
      data: "success"
    }],
    "DELETE": [200, {
      code: 0,
      msg: "OK",
      data: ""
    }]
  }
}

export default mock
