<template>
  <div class="m-auto" style="max-width: 720px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Edit <span style="font-family: 'Arial Black', serif">Event</span>
      </h2>
      <form @submit.prevent="updateEvent()">
        <div class="row p-2">
          <div class="col-6">
            <div class="mb-2">
              <label for="title" class="form-label">Title</label>
              <input
                type="text"
                class="form-control"
                id="title"
                v-model="eventForm.title"
                required
              />
            </div>
            <div class="mb-2">
              <label for="startingDate" class="form-label">
                <i class="bi bi-calendar-week-fill"></i>
                Starting Date & Time
              </label>
              <input
                type="datetime-local"
                class="form-control"
                id="startingDate"
                v-model="eventForm.startingDate"
                required
              />
            </div>
            <div class="mb-2">
              <label for="endingDate" class="form-label">
                <i class="bi bi-calendar-week-fill"></i>
                Ending Date & Time
              </label>
              <input
                type="datetime-local"
                class="form-control"
                id="endingDate"
                v-model="eventForm.endingDate"
                required
              />
            </div>
            <div class="mb-2">
              <label for="category" class="form-label">
                <i class="bi bi-ticket-perforated-fill"></i>
                Category
              </label>
              <div class="row">
                <div class="col">
                  <div class="form-check">
                    <input
                      type="radio"
                      class="form-check-input me-1"
                      id="existingCategory"
                      name="categoryType"
                      v-model="eventForm.existingCategoryChecked"
                      :value="true"
                    />
                    <label for="existingCategory" class="form-check-label">
                      Existing category
                    </label>
                  </div>
                  <select
                    class="form-control form-select"
                    id="category"
                    v-model="eventForm.existingCategoryId"
                    :disabled="!eventForm.existingCategoryChecked"
                    required
                  >
                    <option
                      v-for="eventCategory in eventCategories"
                      :key="eventCategory.id"
                      :value="eventCategory.id"
                    >
                      {{ eventCategory.name }}
                    </option>
                  </select>
                </div>
                <!--                <div class="col-6">-->
                <!--                  <div class="form-check">-->
                <!--                    <input-->
                <!--                      type="radio"-->
                <!--                      class="form-check-input me-1"-->
                <!--                      id="newCategory"-->
                <!--                      name="categoryType"-->
                <!--                      v-model="eventForm.existingCategoryChecked"-->
                <!--                      :value="false"-->
                <!--                    />-->
                <!--                    <label for="newCategory" class="form-check-label">-->
                <!--                      New category-->
                <!--                    </label>-->
                <!--                  </div>-->
                <!--                  <input-->
                <!--                    type="text"-->
                <!--                    class="form-control"-->
                <!--                    id="categoryNew"-->
                <!--                    v-model="eventForm.newCategory"-->
                <!--                    :disabled="eventForm.existingCategoryChecked"-->
                <!--                    required-->
                <!--                  />-->
                <!--                </div>-->
              </div>
              <!-- Info card -->
              <!--              <div v-if="!isAdmin">-->
              <!--                <div-->
              <!--                  class="row p-1 m-1 mt-2 bg-info"-->
              <!--                  style="border-radius: 5px"-->
              <!--                >-->
              <!--                  <p class="mt-1">-->
              <!--                    <i class="bi bi-info-circle-fill"></i> New categories will-->
              <!--                    be be reviewed by administrators before they become-->
              <!--                    available-->
              <!--                  </p>-->
              <!--                </div>-->
              <!--              </div>-->
              <!-- **** -->
            </div>
            <div class="mb-2">
              <label for="location" class="form-label">
                <i class="bi bi-geo-alt-fill"></i>
                Location
              </label>
              <div class="input-group">
                <span class="input-group-text">
                  <i class="bi bi-search"></i>
                </span>
                <input
                  type="search"
                  class="form-control"
                  id="location"
                  v-model="locationInput"
                  list="locations-list"
                  @input="autocompleteLocation"
                  required
                />
                <datalist id="locations-list" v-if="locationList.length > 0">
                  <option selected value="">(all)</option>
                  <option
                    v-for="location in locationList"
                    :key="location"
                    :value="location.label"
                  >
                    {{ location.label }}
                  </option>
                </datalist>
              </div>
            </div>
            <div class="mb-2">
              <label for="locationDetails" class="form-label">
                Location Details (floor, etc.) (optional)
              </label>
              <input
                type="text"
                class="form-control"
                id="locationDetails"
                v-model="eventForm.locationDetails"
              />
            </div>
            <div class="mb-2">
              <label for="description" class="form-label"> Description </label>
              <textarea
                class="form-control"
                id="description"
                v-model="eventForm.description"
                rows="4"
                required
              />
            </div>
            <!-- Administration fields -->
            <div v-if="isAdmin" class="mb-2">
              <label for="status" class="form-label"> Event Status </label>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="status-radio"
                  id="unreviewed-radio"
                  value="UNREVIEWED"
                  v-model="eventForm.status"
                />
                <label class="form-check-label" for="unreviewed-radio">
                  Unreviewed
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="status-radio"
                  id="published-radio"
                  value="PUBLISHED"
                  v-model="eventForm.status"
                />
                <label class="form-check-label" for="published-radio">
                  Published
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="status-radio"
                  id="rejected-radio"
                  value="REJECTED"
                  v-model="eventForm.status"
                />
                <label class="form-check-label" for="rejected-radio">
                  Rejected
                </label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="status-radio"
                  id="cancelled-radio"
                  value="CANCELLED"
                  v-model="eventForm.status"
                />
                <label class="form-check-label" for="cancelled-radio">
                  Cancelled
                </label>
              </div>
            </div>
            <div v-if="isAdmin" class="mb-2">
              <label for="admin-comments" class="form-label">
                <i class="bi bi-chat-text-fill"></i>
                Administrator Comments (optional)
              </label>
              <textarea
                class="form-control"
                id="admin-comments"
                v-model="eventForm.adminComments"
              />
            </div>
            <div v-if="isAdmin" class="mb-2">
              <label for="cancellation-reason" class="form-label">
                <i class="bi bi-calendar-x-fill"></i>
                Cancellation Reason (optional)
              </label>
              <textarea
                class="form-control"
                id="cancellation-reason"
                v-model="eventForm.cancellationReason"
                :disabled="eventForm.status !== 'CANCELLED'"
              />
            </div>
            <!-- **** -->
            <button
              type="submit"
              class="btn btn-primary mt-2 me-2"
              @click="eventForm.error = null"
            >
              Update Event
            </button>
            <button
              class="btn btn-secondary mt-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <!-- Info card -->
            <div
              v-if="
                !(isAdmin || isVerifiedUser) && this.event.status === 'REJECTED'
              "
              class="row p-1 m-1 mt-3 bg-warning"
              style="border-radius: 5px"
            >
              <p class="mt-1">
                <i class="bi bi-exclamation-triangle-fill"></i> If you update
                this event, it will be sent for reviewing again
              </p>
            </div>
            <!-- **** -->
            <!-- Info card -->
            <div
              v-if="
                !(isAdmin || isVerifiedUser) && this.event.status !== 'REJECTED'
              "
              class="row p-1 m-1 mt-3 bg-info"
              style="border-radius: 5px"
            >
              <p class="mt-1">
                <i class="bi bi-info-circle-fill"></i> Since you are an
                unverified user, this event will be reviewed by administrators
                before it becomes available
              </p>
            </div>
            <!-- **** -->
            <hr />
            <span v-if="event.status === 'PUBLISHED' && !isAdmin">
              <button
                class="btn btn-warning me-2"
                @click.prevent
                data-bs-toggle="modal"
                data-bs-target="#cancelEventModal"
              >
                Cancel Event...
              </button>
            </span>
            <span
              v-if="
                event.status === 'UNREVIEWED' ||
                event.status === 'REJECTED' ||
                isAdmin
              "
            >
              <button
                class="btn btn-danger"
                @click.prevent
                data-bs-toggle="modal"
                data-bs-target="#deleteEventModal"
              >
                Delete Event...
              </button>
            </span>
          </div>
          <div class="col-6">
            <ImageSelector
              ref="imageSelector"
              :maxFiles="10"
              :maxFileSize="5 * 1024 * 1024"
              :serverImages="serverImages"
            />
            <div
              class="mt-2"
              v-if="
                eventForm.title === '' ||
                eventForm.coordinateX === '' ||
                eventForm.coordinateY === ''
              "
            >
              <div style="aspect-ratio: 2/3">
                <img
                  src="/map-placeholder.png"
                  class="d-block w-100"
                  style="object-fit: cover; width: 100%; height: 100%"
                  alt="Map placeholder"
                />
              </div>
            </div>
            <div v-else>
              <EventMap
                :events="[eventForm]"
                @popup-closed="popupClosed"
              ></EventMap>
            </div>
            <div
              class="alert alert-danger alert-dismissible fade show mt-3"
              role="alert"
              v-if="eventForm.error"
            >
              <strong>Error:</strong> {{ eventForm.error }}
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Close"
                @click="eventForm.error = null"
              ></button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <!-- Delete Event modal -->
  <div
    class="modal fade"
    id="deleteEventModal"
    tabindex="-1"
    aria-labelledby="deleteEventModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="deleteEventModalLabel">Delete Event</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body text-start">
          <p>
            Are you sure you want to delete the event
            <b>{{ eventForm.title }}</b>
            ? This cannot be undone.
          </p>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button
            type="button"
            class="btn btn-danger"
            data-bs-dismiss="modal"
            @click="deleteEvent"
          >
            Delete Event
          </button>
        </div>
      </div>
    </div>
  </div>
  <!-- **** -->
  <!-- Cancel Event modal -->
  <div
    class="modal fade"
    id="cancelEventModal"
    tabindex="-1"
    aria-labelledby="cancelEventModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="cancelEventModalLabel">Cancel Event</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body text-start">
          <p>
            Are you sure you want to cancel the event
            <b>{{ eventForm.title }}</b>
            ? Subscribed members will be notified.
          </p>
          <div class="mb-2">
            <label for="admin-comments" class="form-label">
              <i class="bi bi-calendar-x-fill"></i>
              Cancellation Reason (optional)
            </label>
            <textarea
              class="form-control"
              id="admin-comments"
              v-model="eventForm.cancellationReason"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button
            type="button"
            class="btn btn-warning"
            data-bs-dismiss="modal"
            @click="cancelEvent"
          >
            Cancel Event
          </button>
        </div>
      </div>
    </div>
  </div>
  <!-- **** -->
</template>

<script>
import { BACKEND_URL, MAPBOX_TOKEN } from "@/constants";
import { getStore } from "@/common/store";
import { MapBoxProvider } from "leaflet-geosearch";
import EventMap from "@/components/events/EventMap.vue";
import EventCategoriesRepository from "@/repositories/EventCategoryRepository";
import EventRepository from "@/repositories/EventRepository";
import ImageSelector from "@/components/ImageSelector";

export default {
  name: "EventEdit",
  props: {
    event: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      locationInput: "",
      locationList: [],
      eventCategories: [],
      serverImages: [],
      eventForm: {
        id: null,
        title: "",
        startingDate: "",
        endingDate: "",
        existingCategoryChecked: true,
        existingCategoryId: null,
        newCategory: null,
        coordinateX: "",
        coordinateY: "",
        locationDetails: "",
        description: "",
        status: "",
        adminComments: "",
        cancellationReason: "",
        error: null,
      },
    };
  },
  components: { ImageSelector, EventMap },
  methods: {
    async autocompleteLocation() {
      if (this.locationInput !== "") {
        const provider = new MapBoxProvider({
          params: {
            access_token: MAPBOX_TOKEN,
            proximity: "ip", //the places closer to the user IP are shown first
          },
        });
        const results = await provider.search({ query: this.locationInput });
        this.locationList = results;
        //take latitude and longitude from first result
        // - coordinateX is the latitude
        // - coordinateY is the longitude
        this.eventForm.coordinateX = results[0].y;
        this.eventForm.coordinateY = results[0].x;
      } else {
        this.eventForm.coordinateX = "";
        this.eventForm.coordinateY = "";
      }
    },
    showEventInMap(event) {
      this.showInMapEvent = event;
    },
    popupClosed() {
      this.showInMapEvent = null;
    },
    async updateEvent() {
      try {
        if (this.$refs.imageSelector.imagesToDelete.length > 0) {
          for (const file of this.$refs.imageSelector.imagesToDelete) {
            const segments = file.url.split("/");
            const idImage = segments.pop();
            await EventRepository.deleteEventImage(this.eventForm.id, idImage);
          }
        }
        if (this.$refs.imageSelector.imagesToUpload.length > 0) {
          for (const image of this.$refs.imageSelector.imagesToUpload) {
            await EventRepository.saveEventImage(this.eventForm.id, image.file);
          }
        }
        await EventRepository.save(this.eventForm);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventForm.error = response.message;
        console.error(err);
      }
    },
    async deleteEvent() {
      try {
        await EventRepository.delete(this.event.id);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.eventForm.error = response.message;
        console.error(err);
      }
    },
    async cancelEvent() {
      this.eventForm.status = "CANCELLED";
      await this.updateEvent();
    },
    async coordinatesToAddress() {
      const provider = new MapBoxProvider({
        params: {
          access_token: MAPBOX_TOKEN,
        },
      });
      const results = await provider.search({
        query: this.event.coordinateY + "," + this.event.coordinateX,
        //the mapbox url uses {longitude, latitude} in that order
      });
      //take address from first result
      return results[0].label;
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
    isAdmin() {
      return getStore().state.user.authority === "ADMIN";
    },
    isVerifiedUser() {
      return getStore().state.user.authority === "USER_VERIFIED";
    },
  },
  async beforeMount() {
    EventCategoriesRepository.findAll().then((response) => {
      this.eventCategories = response;
    });
    this.eventForm.id = this.event.id;
    this.eventForm.title = this.event.title;
    this.eventForm.startingDate = this.event.startingDate;
    this.eventForm.endingDate = this.event.endingDate;
    this.eventForm.existingCategoryId = this.event.category.id;
    this.eventForm.coordinateX = this.event.coordinateX;
    this.eventForm.coordinateY = this.event.coordinateY;
    this.eventForm.locationDetails = this.event.locationDetails;
    this.eventForm.description = this.event.description;
    this.eventForm.status = this.event.status;
    this.eventForm.adminComments = this.event.adminComments;
    this.eventForm.cancellationReason = this.event.cancellationReason;
    this.locationInput = await this.coordinatesToAddress();
    // add images from server:
    if (this.event.numImages > 0) {
      for (let i = 0; i < this.event.numImages; i++) {
        const image = {
          url: `${BACKEND_URL}/events/${this.event.id}/image/${i}`,
          name: "",
        };
        this.$refs.imageSelector.imagesList.push(image);
      }
    }
  },
};
</script>

<style scoped></style>
