import Koa from 'koa'
import compose from "koa-compose"
import logger from "koa-logger"
import koaCors from "@koa/cors"

import mock from './mock'

const myKoaCors = koaCors({
  origin: "*",
  credentials: true,
  allowMethods: ["GET", "HEAD", "PUT", "POST", "DELETE", "PATCH"],
});

// const handle: Koa.Middleware = async (ctx, next) => {
//   await next();
//   ctx.body = {
//     code: 0,
//     data: ctx.body,
//     msg: ctx.message
//   }
// }

const app = new Koa()
app.use(compose([logger(), myKoaCors]))

app.use(async (ctx, next) => {
  const mockData = mock[ctx.url][ctx.method]
  ctx.status = mockData[0]
  ctx.body = mockData[1]
  await next()
})

app.listen(10086, () => console.log("koa is running on port 10086!"))
