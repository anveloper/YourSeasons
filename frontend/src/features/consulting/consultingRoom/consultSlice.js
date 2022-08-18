import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import Axios, { imgAxios } from 'api/Axios'

const initialState = {
  session: undefined,
  customer: undefined,
  isSetClear: false,
  consultantSessionName: 's-s-s',
  consultingId: 0,
  messageId: 2,
  messageList: [
    {
      id: 1,
      role: '',
      imageUrl: '', // '' : mine, '/images/d~' : other
      side: 'left', // '' : other, 'right' : mine
      message: '대화를 시작합니다.'
    }
  ]
}

export const openConsulting = createAsyncThunk(
  'consult/openConsulting',
  async (reservationId, { rejectWithValue }) => {
    try {
      const response = await Axios.post(`consultings`, { reservationId: reservationId })
      console.log(response.data)
      return response.data
    } catch (err) {
      return rejectWithValue(err)
    }
  }
)


export const getConsultantSessionName = createAsyncThunk(
  'consult/getConsultantSessionName',
  async (reservationId, { rejectWithValue }) => {
    try {
      const response = await Axios.post(`consultings/join`, { reservationId: reservationId })
      return response.data
    } catch (err) {
      return rejectWithValue(err)
    }
  }
)

export const postConsultingResult = createAsyncThunk(
  'consult/postConsultingResult',
  async (payload, { rejectWithValue }) => {
    try {
      console.log(payload.files[0], JSON.stringify(payload.consultingFinishRequest))
      let formData = new FormData()
      formData.append('consultingFinishRequest', new Blob([JSON.stringify(payload.consultingFinishRequest)], { type: "application/json" }))
      formData.append('file', payload.files[0])
      const response = await imgAxios.post(`consultings/1`, formData)
      console.log(response.data)
      alert('진단 결과가 저장되었습니다. 컨설팅을 종료합니다.')
      return response.data
    } catch (err) {
      console.log(err)
      return rejectWithValue(err)
    }
  }
)

export const consultSlice = createSlice({
  name: 'consult',
  initialState,
  reducers: {
    settingModalOn: (state) => {
      state.isSetClear = true;
    },
    settingModalOff: (state) => {
      state.isSetClear = false;
    },
    setCustomer: (state, { payload }) => {
      state.customer = payload
    },
    setSession: (state, { payload }) => {
      state.session = payload
    },
    appendMessageList: (state, { payload }) => {
      if (payload.id > state.messageId) {
        state.messageId = payload.id + 1
      } else {
        payload.id = state.messageId
        state.messageId = state.messageId + 1
      }
      state.messageList.push(payload)
    }
  },
  extraReducers: {
    [getConsultantSessionName.fulfilled]: (state, { payload }) => {
      state.consultantSessionName = payload.sessionId
      state.consultingId = payload.consultingId
    },
    [openConsulting.fulfilled]: (state, { payload }) => {
      state.consultantSessionName = payload.sessionId
      state.consultingId = payload.consultingId
    },
  }
})
export const { settingModalOn, settingModalOff, setSession, setCustomer, appendMessageList } = consultSlice.actions;

export default consultSlice.reducer