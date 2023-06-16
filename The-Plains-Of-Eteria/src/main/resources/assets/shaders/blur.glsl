uniform float _deltaTime;
uniform float _unscaledDeltaTime;
uniform float _timeSinceStart;

uniform mat4 _Transform;

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
    gl_Position = _Transform * vec4(_pos, 1);
}

#FRAGMENT
in vec4 fColor;
in vec2 fUV;
out vec4 color;

vec3 HUEtoRGB(float H)
{
    float R = abs(H * 6 - 3) - 1;
    float G = 2 - abs(H * 6 - 2);
    float B = 2 - abs(H * 6 - 4);
    return clamp(vec3(R,G,B), 0.0f, 1.0f);
}

vec3 HSVtoRGB(vec3 HSV)
{
    vec3 RGB = HUEtoRGB(HSV.x);
    return ((RGB - 1.0f) * HSV.y + 1.0f) * HSV.z;
}

void main(){
    vec2 uv = fUV * _tiling + _offset;
    uv.y = -uv.y;
    vec4 texColor = texture(_mainTex, uv);

    vec3 col = texColor.rgb;
    float pos = (sin(_timeSinceStart) + 1) / 2;
    vec3 hsvCol = HSVtoRGB(vec3(pos, 1.0f, 1.0f));
    color = vec4(col * hsvCol, 1.0f);
}