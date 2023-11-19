// 1. select a user by UID
SELECT * FROM Users WHERE UID = 1;

// 2. Find a user by eamil
SELECT * FROM Users WHERE Eamil = 'alice@email.com':

// 3. Limit to 10 users
SELECT * FROM Users LIMIT 10;

// 4. Get all users names that start witg 'D'
SELECT * FROM Users WHERE Email LIKE 'D%';

// 5. Get all users who are less than 50
SELECT * FROM Users WHERE Age < 50;

// 6. get all users who are greater than 50
SELECT * FROM Users WHERE Age > 50;

// 7. get all usets who are between 20 and 100
SELECT * FROM Users WHERE Age >= 20 && Age <= 100;

// 8. ger all users who are 28 and live in Berlin
SELECT * FROM Users WHERE Age = 28 && Location = 'Berlin';

/*
{
    "users": {
      "1": {
        "name": "David",
        "email": "david@email.com",
        "age": 99,
        "location": "SF",
        "age_location": "99_SF"
      },
      "9": {
        "name": "Alice",
        "email": "alice@email.com",
        "age": 28,
        "location": "Berlin",
        "age_location": "28_Berlin"
      }
    }
  }
  */

  const rootRef = firebase.database().ref();

  // 1. select a user by UID
const oneRef = rootRef.child('users').child('1');

// 2. Find a user by eamil
const twoRef = rootRef.child('users').orderByChild('email').equalTo('alice@email.com');

// 3. Limit to 10 users
const threeRef = rootRef.child('users').orderByKey().limitToFirst(10);
    // we can get rid of this
const threeRef2 = rootRef.child('users').limitToFirst(10);

// 4. Get all users names that start witg 'D'
    // Sometimes there are problems with dealing with Unicode.
    // And so you want tomake sure you're getting back everythingwithin the Unicode range.
    // uf8ff is at one of the highest poinets in the Unicode
    // startAt and endAt make sure that you'regrabbing everything that starts with the letter D.
const fourRef = rootRef.child('users').orderByChild('name').startAt('D').endAt('D\uf8ff');

// 5. Get all users who are less than 50
const fiveRef = rootRef.child('users').orderByChild('age').endAt(49);

// 6. get all users who are greater than 50
const sixRef = rootRef.child('users').orderByChild('age').startAt(51);

// 7. get all usets who are between 20 and 100
const sevenRef = rootRef.child('users').orderByChild('age').startAt(20).endAt(100);

// 8. ger all users who are 28 and live in Berlin
  // note that we only can use 1 ordering function
  // so how we get user
  // we can optimize datastructure for the questions
const eightRef = rootRef.child('users').orderByChild('age').equalTo(28)
                                        .orderByChild('location').equalTo('Berlin');
                
const eightRef2 = rootRef.child('users').orderByChild('age_location').equalTo("28_Berlin");

                                        