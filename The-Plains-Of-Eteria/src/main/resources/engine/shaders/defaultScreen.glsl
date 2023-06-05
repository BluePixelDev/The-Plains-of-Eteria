uniform float _deltaTime;
uniform float _unscaledDeltaTime;
uniform float _timeSinceStart;

uniform vec2 _tiling = vec2(1);
uniform vec2 _offset;
uniform sampler2D _mainTex;

#VERTEX
layout(location=0) in vec3 _pos;
layout(location=1) in vec4 _color;
layout(location=2) in vec2 _uv;

out vec4 fColor;
out vec2 fUV;

void main(){
    fColor = _color;
    fUV = _uv;
    gl_Position = vec4(_pos, 1);
}

#FRAGMENT
in vec4 fColor;
in vec2 fUV;

out vec4 color;

void main(){
    vec2 uv = fUV * _tiling + _offset;
    uv = vec2(uv.x, -uv.y);

    vec4 texColor = texture(_mainTex, uv);
    color = texColor;
}