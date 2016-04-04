# Notes #

I'm not too sure about how much you're expecting in this, so I thought I'd write a few notes about what I did and what
my thoughts were in case it helps.

## Running the application ##

  * Run lein ring server
  * Run curl -X POST -H "Accept: text/plain" -H "Content-Type: application/json" http://localhost:3000/translate -d '{"number":"23"}'

## General development process ##

I've only done a couple of toy REST services in Clojure so I still haven't got a feel for what best practice is, or
which frameworks would be best suited to this. I used Ring and Compojure because I've used them in the past. I didn't
explore whether there's anything better out there.

I first got a basic hello-world REST service up and running, then TDD'd the number conversion without hooking it up to
the REST service, then filled in the REST service and wrote some integration tests for it. I think it's worth having
separate tests for the number conversion because it's sufficiently complicated that you'd want to test it in isolation.

## Code ##

### Files ###

  * numbers.clj - Converting numbers to words
  * rest.clj - REST API

I've been using Stuart Sierra's Reloaded workflow on other projects and wanted to use it here, so there are user.clj and
system.clj files for the workflow. I haven't found a good way to integrate the workflow with Ring, but it was helpful to
me for the number-to-word conversion code.

## Converting numbers to words ##

I spent a while circling around this trying to find a nicer approach but failed to find one I'm entirely happy with. I
TDD'd the code, writing the test cases first and then trying various approaches to convert the numbers, but there seemed
to be a lot of edge cases and I couldn't see a neat way to write the code without including a lot of conditional logic.
In the end I just bundled the logic up into functions to make it a little clearer and to keep it tidy, but I couldn't
see a way to get rid of that complexity.

There is a bit of repetition in num-to-words and convert-less-than-one-thousand but it didn't seem useful to factor that
out at this point. I suspect that the pattern for handling numbers more than one thousand will be slightly different, if
that were a future requirement, so factoring out a common pattern at this point seemed premature.

The implementation only works for numbers under a thousand so I attached a precondition to catch anyone using it
accidentally.

The tests are given/when/then (or arrange/act/assert) style, and I've tried to reflect the test scenario in the test
names.

## REST API ##

I wasn't too sure how best to model this as this didn't seem to be the usual CRUD REST API that I'm more familiar with.
In the end I just went for a POST to a translate URL to convert the numbers.

### POST /translate ###

Translates the specified number to words and returns it, with SC_OK if successful or SC_BAD_REQUEST if the parameter
cannot be parsed, is missing, or is out of range.

I wasn't too sure how best to catch exceptions and translate them to status codes. I've done this regularly in Java but
don't really have a good idea of how best to do it in Clojure. I think this is a cross-cutting concern, so I wanted
something common to all methods. In the end I settled on having the method catch any expected exception, and re-throw a
specific internal exception which is then caught by a middleware function and converted into a status code. It seems a
little convoluted in such a small example, but it's similar to what I've done in Java previously and it worked
reasonably well there. It would allow code in any part of the server to throw an exception to halt execution, but the
error would still be neatly translated for the client.

I didn't TDD the REST service -- I manually tested while putting it together then wrote tests after it was up and
running to check various scenarios.

# Hacky limitations #

There are a few limitations that occurred to me while doing this that I'd probably want to look at if this were
production ready code:

  * I haven't looked at securing Jetty
  * I haven't looked at configuring logging in Jetty
  * Internal coding errors will result in the stack trace being dumped to the client, which is ugly. These should
    probably be caught and transformed into SC_INTERNAL_SERVER_ERROR in the same way as out-of-range parameters are
  * Media types aren't being checked in the tests or the implementation and probably should be
  * There's no versioning of the REST API
  * I haven't looked at localsation and so on

# Final thoughts #

I just wanted to say that I enjoyed this, and thanks for your time.
