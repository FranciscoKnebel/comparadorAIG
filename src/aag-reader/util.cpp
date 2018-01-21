#include "util.h"

bool Util::isOdd(int number) {
	return (number % 2);
}

bool Util::isEven(int number) {
	return !isOdd(number);
}
