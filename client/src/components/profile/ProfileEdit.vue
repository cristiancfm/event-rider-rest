<template>
  <div class="m-auto" style="max-width: 720px">
    <div class="text-start p-2">
      <h2 class="m-2">
        Edit <span style="font-family: 'Arial Black', serif">Profile</span>
      </h2>
      <div class="row p-2">
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
          <div class="text-center">
            <button
              class="btn btn-secondary mt-3 mb-1"
              @click.prevent="startFileUpload()"
            >
              Change image...
            </button>
            <span v-if="imageMessage">{{ imageMessage }}</span>
            <input
              ref="hiddenInput"
              type="file"
              accept="image/*"
              class="d-none"
              @change="updateFileUpload()"
            />
          </div>
        </div>
        <div class="col-9">
          <form @submit.prevent="updateProfile()">
            <div class="mb-2">
              <label for="name" class="form-label">Name / Organization</label>
              <input
                type="text"
                class="form-control"
                id="name"
                v-model="profileForm.name"
                required
              />
            </div>
            <div class="mb-2">
              <label for="surname" class="form-label">Surname (optional)</label>
              <input
                type="text"
                class="form-control"
                id="surname"
                v-model="profileForm.surname"
              />
            </div>
            <div class="mb-2">
              <label for="biography" class="form-label">Biography</label>
              <textarea
                class="form-control"
                id="biography"
                v-model="profileForm.biography"
              />
            </div>
            <button
              type="submit"
              class="btn btn-primary mt-2"
              @click="profileForm.error = null"
            >
              Update Profile
            </button>
            <button
              class="btn btn-secondary mt-2 ms-2"
              @click.prevent="$router.go(-1)"
            >
              Cancel
            </button>
            <div
              class="alert alert-danger alert-dismissible fade show mt-3"
              role="alert"
              v-if="profileForm.error"
            >
              <strong>Error:</strong> {{ profileForm.error }}
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert"
                aria-label="Close"
                @click="profileForm.error = null"
              ></button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { BACKEND_URL } from "@/constants";
import { getStore } from "@/common/store";
import auth from "@/common/auth";
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "ProfileEdit",
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      imageMessage: null,
      profileForm: {
        id: null,
        name: null,
        surname: null,
        biography: null,
        error: null,
      },
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
    async updateProfile() {
      try {
        if (this.$refs.hiddenInput.files.length > 0) {
          await UserRepository.saveUserImage(
            this.user.id,
            this.$refs.hiddenInput.files[0]
          );
        }
        await auth.update(this.profileForm);
        this.$router.push("/profile");
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.profileForm.error = response.message;
        console.error(err);
      }
    },
    updateFileUpload() {
      this.imageMessage = this.$refs.hiddenInput.files[0].name;
    },
    startFileUpload() {
      this.$refs.hiddenInput.click();
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  mounted() {
    this.profileForm.id = this.user.id;
    this.profileForm.name = this.user.name;
    this.profileForm.surname = this.user.surname;
    this.profileForm.biography = this.user.biography;
  },
};
</script>

<style scoped></style>
