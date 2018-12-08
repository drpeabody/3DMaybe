#version 130

uniform sampler2D texture;
in float TexDominance;
in vec2 texCood0;
in vec4 color0;

out vec4 fragColor;

void main(){
    vec4 v = texture2D(texture, texCood0.st);
    fragColor = TexDominance*v + color0;
}