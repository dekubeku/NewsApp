# NewsApp
Using the developer verison of the NewsAPI, I have created an Android app that takes user input in the form of a news website domain (bbc.co.uk, techcrunch.com, www.svt.se ...) and outputs the top articles from the given source in a list. The user may also click on a specific article in that list in order to access the full article.

#### Setup process
Create an XML-file with your NewsAPI key and add it to res/values:

      <?xml version="1.0" encoding="utf-8"?>
      <resources>
         <string name="key">"YOUR_KEY"</string>
      </resources>

## App overview
The app mainly consists of three activities: ```MainActivity```, ```SearchList```, and ```Article```. The user starts out in the ```MainActivity``` where they are asked to input the news domain. That input is then sent to the second activity, ```SearchList``` where most of the code is. The largest methods in ```SearchList``` and their functionality are:

*```getArticles``` - Performs an HTTP-request throught the NEWSApi and returns a JSONObject containing the data we want. From this JSONObject we extract a JSONArray that specifically holds the all the articles within each element.

*```jsonToListItem``` - From the JSONArray we got from the previous method, we extract the data we want from each article and make them into an article-items. (```Item.java```) With these items we then populate the ListView so that the user may scroll through the articles and interact with them.

*```getArticleImage``` - The ImageUrl from the JSONObject for each article is used to perform yet another HTTP request. The returned image is then assigned to the item it belongs to, and then updates the list.

When an article item is clicked, the user is taken to the ```Article``` activity, where the content from the full article is displayed.


## Some features not mentioned above
* In the ```SearchList```, the information displayed about each article is: Title, Image, Author, Date, and a short description.
* User may navigate to the news website from the ```Article``` activity if they want to read the article there instead.
* While scrolling through the list of articles, more articles load automatically when bottom is reached.
* App has a nice dark theme that doesn't hurt the users eyes, wow!

## Some drawbacks/compromises
1. Since I only have access to the free developer verison of the NewsAPI I could not access the full content of each article and could only print a truncated version with 200~ characters. However, this is easily dealt with by paying for the full version. So in order to emulate a longer article in the full-article view, I simply appended many of the truncated versions with eachother. I considered doing another HTTP request as a work around, but considering that it would not be necessary later on (with the full version), I decided against it.

2. At this point in time, the input from the user has to be spelled exactly correct as the input String is directly put into the request to the API. In the future this would ideally be implemented as an actual search bar, that would give suggestions, correct spelling mistakes etc.

## Biggest problems that arose along the way
1. While first trying to use the NewsAPI in Android it gave me a status message of 403: Forbidden, and I got stumped. I tried using the API in Python and it worked perfectly. I figured it was because of the developer version not wanting to let me use the API because it was used on an emulator/phone. After a bit of sleuthing I found that I could add this to the request:

   ```@Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }
            
This "tricks" the API into thinking that the person is making the request from a browser instead and enables the request. However, this would not be necessary in an app with access to the full version of NewsAPI, but as this was integral to showcasing the apps functionality I decided a work around was necessary.

2. When the user clicks on a specific article the the list the same image displayed in the list should also appear in the full view. I tried to make each article item parcelable in order to be able to access it from the full article activity. However, the image was too large and could therefore not be "sent". My first solution was to resize the image to become smaller before sending it, and then make it bigger again upon arrival. This seemed to work, and the resizing didn't seem to affect the image quality very much at all. 

   However, when the user would return from the full article view, the image in the list would be the smaller size, so I realised that that solution was a bit silly. I decided, that instead of sending the entire article-object to the new activity I would just send the indiviual data through the intents instead. This was my original solution while testing to only send the String data, but I thought that it might be cleaner to send the object when it was time to access the article image. I converted the Bitmap image into a ByteArray and "sent it" that way.

3. I had a bit of trouble with populating my list of articles with the associated image. Some of the images wouldn't load on time when it was time for the list of articles to be populated. I originally thought I had set up the code in a way that would ensure that ```jsonToListItem()``` method only runs after all the images have been returned from the HTTP-request in ```getArticleImage()```. However, this did not work. I fixed this by switching the order I called each method, and letting the image in each article items temporarily be null until it was assigned (or not). Then, inside ```getArticleImage()```, I called ```adapter.notifyDataSetChanged();``` in order to update the items in the list when their image had been loaded.

## Conclusion
Overall, I think the app looks quite nice and I'm happy with the outcome. I got to try many new things in this project like working with an API for the first time. I know that there are probably a few things I could have done better; but for being a ~5 day project I am satisfied.
