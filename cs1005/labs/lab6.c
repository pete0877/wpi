#include <stdio.h>
#include <ctype.h>

void cap(char instring[], char outstring[]);

void main() 
{
	char input[80],output[80];
	printf("\n\nPlease enter a string:");
	gets(input);
	cap(input,output);
	printf("With volwels capitalized: %s\n\n",output);
}

void cap(char instring[], char outstring[])
{
	int a,b;
	char z;
	char * p1;
	char * p2;
	p1=instring;
	p2=outstring;

	b=strlen(instring);
	for(a=0;a<b;a++)
	{
		z=*p1;
		p1++;
		switch (z)
		{
			case 'a':
			case 'e':
			case 'i':
			case 'I':
			case 'o':
			case 'O':
			case 'u':
				z=toupper(z);
				break;			
		}
		*p2=z;
		p2++;
	}
}


