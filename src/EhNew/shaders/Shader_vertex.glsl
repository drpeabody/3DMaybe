#version 130

layout (location = 0) in vec4 Position;
layout (location = 1) in vec2 textCood;
layout (location = 2) in vec3 Normal;
layout (location = 3) in vec3 tangent;

uniform mat4 trans = mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
uniform mat4 proj = mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
uniform mat4 cam = mat4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

out vec2 TextureCood0;
out vec3 Normal0;
out vec3 WorldFragPos0;
out vec3 Tangent0;

void main(){
    //gl_Position = proj * cam * trans * Position + gl_Position;
    gl_Position = proj * cam * trans * Position;
    TextureCood0 = textCood;
    Normal0 = (trans * vec4(Normal, 0.0)).xyz;
    Tangent0 = normalize(trans * vec4(tangent, 0.0)).xyz;
    WorldFragPos0 = (trans * Position).xyz;
}
