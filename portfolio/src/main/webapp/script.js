// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* global google */


let navbar;
let navOffset;
let map;
let markersCreated;

window.onload = function() {
  navbar = document.getElementById('navbar');
  navOffset = navbar.offsetTop;
  getComments();
  createMap();
  markersCreated = 0;
};

window.onscroll = function() {
  makeNavSticky();
};

function makeNavSticky() {
  if (window.pageYOffset >= navOffset) {
    navbar.classList.add('sticky');
  } else {
    navbar.classList.remove('sticky');
  }
}

/**
 * Adds a random Avatar line to the page.
 */
function addRandomLine() {  // eslint-disable-line no-unused-vars
  const lines = [
    'That\'s rough buddy.',
    'My cabbages!',
    'Flameo, hotman!',
    'Yip yip!',
    `Instead of seeing what they want you to see, you got to open your brain
     to the possibilities.`,
    'Life happens wherever you are, whether you make it or not.',
    'Boomerang! You do always come back!',
    'It\'s a giant mushroom! Maybe it\'s friendly!',
    'I am not Toph! I am Melon-Lord! Muah-ha-ha-ha-ha!',
  ];

  // Pick a random line.
  const line = lines[Math.floor(Math.random() * lines.length)];

  // Add it to the page.
  const container = document.getElementById('avatar-container');
  container.innerText = line;
}

function getComments() {
  const maxComments = document.getElementById('max-comment-select').value;
  fetch('/comment?max-comments=' + maxComments)
      .then((response) => response.json())
      .then((comments) => {
        console.log('Comments contained in servlet: ' + comments);
        const commentEl = document.getElementById('comment-list');
        removeAllChildNodes(commentEl);
        comments.forEach((comment) => {
          commentEl.appendChild(createListElement(comment));
        });
      });
}

function removeAllChildNodes(parent) {
  while (parent.firstChild) {
    parent.removeChild(parent.firstChild);
  }
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function postComment(e) {  // eslint-disable-line no-unused-vars
  const commentBody = document.getElementById('comment-body').value;
  const request =
      new Request('/comment?comment-body=' + commentBody, {method: 'POST'});
  fetch(request).then(() => getComments());

  // Clear input form
  document.getElementById('comment-body').value = '';

  // cancel default event action (page refresh)
  e = e || window.event;
  e.preventDefault();
}

function deleteCommentsTwice() {  // eslint-disable-line no-unused-vars
  deleteComments();
  deleteComments();
}

function deleteComments() {
  const request = new Request('/delete-comment', {method: 'POST'});
  fetch(request).then(() => getComments());
}

/**
 * Selects the contents of the 'About' section.
 */
function setAboutContentTo(section) {  // eslint-disable-line no-unused-vars
  document.getElementById('background').style.display = 'none';
  document.getElementById('interests').style.display = 'none';
  document.getElementById('projects').style.display = 'none';

  document.getElementById(section).style.display = 'block';
}

/**
 * Selects the contents of the 'Mood' section.
 */
function setMoodTo(mood) {  // eslint-disable-line no-unused-vars
  document.getElementById('happy').style.display = 'none';
  document.getElementById('sleepy').style.display = 'none';
  document.getElementById('focused').style.display = 'none';
  document.getElementById('energetic').style.display = 'none';
  document.getElementById('goofy').style.display = 'none';

  document.getElementById(mood).style.display = 'block';
}

function createMap() {
  map = new google.maps.Map(
      document.getElementById('map'), {center: {lat: 20, lng: 0}, zoom: 1});

  google.maps.event.addListener(map, 'click', function(event) {
    postMarker(event.latLng);
  });

  getMarkers();
}

function getMarkers() {
  fetch('/marker').then((response) => response.json()).then((markerJsons) => {
    markerJsons.forEach((marker) => {
      placeMarker(JSON.parse(marker));
    });
  });
}

function postMarker(location) {
  placeMarker(location);
  const request =
      new Request('/marker?json=' + JSON.stringify(location), {method: 'POST'});
  fetch(request);
  markersCreated++;
}

function placeMarker(location) {
  new google.maps.Marker({position: location, map: map});
}

/**
 * Deletes the most recent marker placed. Only markers placed since the last
 * page refresh can be deleted.
 */
function deleteRecentMarker() {  // eslint-disable-line no-unused-vars
  if (markersCreated > 0) {
    const request = new Request('/delete-marker', {method: 'POST'});
    fetch(request).then(() => createMap());
    markersCreated--;
  }
}
