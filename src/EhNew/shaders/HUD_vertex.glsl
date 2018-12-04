#version 330

layout (location = 0) in vec2 Position;
layout (location = 1) in vec2 textCood;
layout (location = 2) in vec4 VertColor;
layout (location = 3) in float TexD;

out vec2 texCood0;
out vec4 color0;
out float TexDominance;

void main(){
    gl_Position = vec4(Position,0,1);
    texCood0 = textCood;
    color0 = VertColor;
    TexDominance = TexD;
}