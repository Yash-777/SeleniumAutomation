// injectStringFun.txt

var varFun = function () {
	console.log('Variable Funciton called...');

	fun();
}
function fun () {
	console.log('Normal Funciton...');
}
document.docFun = function () {
	console.log('Document Funciton...');

	fun();
	varFun();
}
/*
 * Form java side using selenium if you wish to call function with arguments
 * then use variable functions.

document.docFun();
Document Funciton...
Normal Funciton...
Variable Funciton called...
Normal Funciton...
*/