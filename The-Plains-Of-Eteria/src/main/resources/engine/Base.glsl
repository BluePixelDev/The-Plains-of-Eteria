#shader "engine/base"

uniform float _deltaTime;
uniform float _unscaledDeltaTime;
uniform float _timeSinceStart;

uniform mat4 _viewMatrix;
uniform mat4 _projectionMatrix;
uniform mat4 _transformMatrix;

uniform vec3 _WorldSpaceCameraPos;
uniform vec4 _ProjectionParams;
uniform vec4 _ScreenParams;

uniform float _Time;

//Vertex attributes
layout(location=0) in vec3 _pos;
layout(location=1) in vec4 _color;
layout(location=2) in vec2 _uv;


//---- SATURATE-METHODS ----
vec3 saturate(in vec3 value){
    return clamp(value, 0.0, 1.0);
}
vec4 saturate(in vec4 value){
    return clamp(value, 0.0, 1.0);
}

#VERTEX
void main(){

}

#FRAGMENT
void main(){

}