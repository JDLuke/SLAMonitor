# SLAMonitor
I was inspired to develop this based on the last project I worked on.

The idea is to have a simple annotation which can be wrapped around any method call.  There will be a maximum 
execution time allowed for said call, and if that time is exceeded an error level log message will be generated.

In addition, there is an optional warning time which can be used to identify problems before they actually happen.

Configure your logging system properly and it becomes a lot easier to develop a console view of actual problems
as well as developing ones.

Since response times are such a critical issue, I'm also planning to add another annotation designed to extract and 
store timing data for analytical purposes.
