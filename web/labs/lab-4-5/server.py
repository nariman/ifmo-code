"""
Simple server for photo saving.
"""

import uuid
import os

import aiohttp_cors
from aiohttp import web


ROOT = os.path.dirname(os.path.abspath(__file__))
PHOTOS = os.path.join(ROOT, "photos")
STATIC = os.path.join(ROOT, "static")

app = web.Application(debug=True)
cors = aiohttp_cors.setup(app, defaults={
    "*": aiohttp_cors.ResourceOptions(
        allow_credentials=True,
        expose_headers="*",
        allow_headers="*",
    )
})

async def upload(request):
    reader = await request.multipart()
    photo_quote = await reader.next()

    photo_id = str(uuid.uuid4())

    size = 0
    with open(os.path.join(PHOTOS, photo_id + ".png"), "wb") as f:
        while True:
            chunk = await photo_quote.read_chunk()  # 8192 bytes by default.
            if not chunk:
                break
            size += len(chunk)
            f.write(chunk)

    return web.json_response(data={
        "id": photo_id,
        "size": size
    })

cors.add(app.router.add_static("/photo/", path=PHOTOS))
cors.add(app.router.add_static("/", path=STATIC))
cors.add(app.router.add_post("/", upload))

web.run_app(app, port=8080)
