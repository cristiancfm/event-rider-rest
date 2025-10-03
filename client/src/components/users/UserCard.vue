<template>
  <div class="card m-2 p-3">
    <div class="row">
      <div class="col-3">
        <div style="aspect-ratio: 1/1">
          <img
            :src="getImageSrc()"
            class="d-block w-100"
            style="object-fit: cover; width: 100%; height: 100%"
            alt="Profile image"
            @error="setPlaceholder"
          />
        </div>
      </div>
      <div class="col-5">
        <p>
          <router-link :to="'/members/' + user.id">
            {{ user.name }}{{ user.surname ? " " + user.surname : "" }}
          </router-link>
          <span class="ms-2" v-if="user.authority === 'USER_VERIFIED'">
            <i class="bi bi-patch-check-fill"></i>
          </span>
          <span class="ms-2" v-if="user.authority === 'ADMIN'">
            <i class="bi bi-person-badge-fill"></i>
          </span>
        </p>
        <p class="text-secondary">
          {{ user.upcomingHostedEvents.length }}
          <span v-if="user.upcomingHostedEvents.length === 1"
            >upcoming event</span
          >
          <span v-else>upcoming events</span>
        </p>
        <p class="text-secondary">
          {{ user.upcomingHostedEvents.length + user.pastHostedEvents.length }}
          <span v-if="user.hostedEvents.length === 1">total event</span>
          <span v-else>total events</span>
        </p>
      </div>
      <div class="col-4 text-end">
        <!-- Follow button -->
        <button
          class="btn btn-secondary m-1"
          @click="followUser"
          v-if="isLogged && !isFollowed && !editUsers"
        >
          <i class="bi bi-person-plus"></i> Follow
        </button>
        <button
          class="btn btn-secondary m-1"
          @click="followUser"
          v-if="isLogged && isFollowed && !editUsers"
        >
          <i class="bi bi-person-plus-fill"></i> Followed
        </button>
        <router-link
          class="btn btn-secondary m-1"
          to="/login"
          active-class="active"
          v-if="!isLogged"
        >
          <i class="bi bi-person-plus"></i> Follow
        </router-link>
        <!-- **** -->
        <!-- Edit button -->
        <router-link
          class="btn btn-secondary m-1"
          :to="`/members/${this.user.id}/edit`"
          active-class="active"
          v-if="isLogged && editUsers"
        >
          <i class="bi bi-pencil-fill"></i> Edit...
        </router-link>
        <!-- **** -->
      </div>
    </div>
    <!-- Suspended info card -->
    <div
      class="row pt-2 m-0 bg-danger"
      style="border-radius: 5px"
      v-if="user.authority === 'USER_SUSPENDED'"
    >
      <p><i class="bi bi-slash-circle-fill"></i> <b>Suspended</b></p>
    </div>
    <!-- **** -->
  </div>
</template>

<script>
import { BACKEND_URL } from "@/constants";
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "UserCard",
  props: {
    user: {
      type: Object,
      required: true,
    },
    editUsers: {
      // whether to show edit button or not
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      isFollowed: false,
    };
  },
  methods: {
    getImageSrc() {
      if (this.user.image) {
        return `${BACKEND_URL}/users/${this.user.id}/image`;
      }
      return "/profile-placeholder.jpg";
    },
    setPlaceholder(event) {
      event.target.src = "/error-placeholder-square.png";
    },
    async followUser() {
      this.$emit("followers", this.user);
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  watch: {
    "user.followers": {
      handler: async function (newFollowers) {
        if (this.isLogged) {
          const account = await UserRepository.findOneBase(
            getStore().state.user.id
          );
          const index = newFollowers.findIndex(
            (follower) => follower.id === account.id
          );
          this.isFollowed = index >= 0;
        }
      },
      immediate: true,
      deep: true,
    },
  },
};
</script>

<style scoped></style>
