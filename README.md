# MyTxtReader

Main classes of a 8-year-ago project reviewed and updated, and updating continuing...

There are still some uncleaned and unstructual codes and the method to read backward is also not that
accurate, more updates are under development, i.e. the book mark gadgets

res/anim/show.xml

res/anim/hidden.xml

background.xml: a testing layout for popup window

Maybe a better way to handle file content is to load all the content the first time the app is launched and store the content in a char array or charSquence and then manipulate the char[] or CharSequence, which is not only faster but also flexible than the method used in the FileProcessor

Some little bugs are cleaned and the background images are from the internet and haven't been processed (gimp for linux and ps for Windows are great tools for this job), thus some of them are a little bigger

Dictionary, the initial inspiration for the project

Interface the dicts to modulize it (Chinese.java, Dictionary.java, English.java)

A better architecture may be define a menuSetter class as an inner class of the TextViewer class so that there aren't so many statics which may seem a little wired. However, in consideration of extensibility, define the setters separately, though they seem a little redundant, is a good choice.

Event operation is optimized: flip to next or previous page, long touch to show menu
